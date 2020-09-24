/*-
 * #%L
 * This file is part of "Apromore Core".
 * %%
 * Copyright (C) 2018 - 2020 Apromore Pty Ltd.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
package org.apromore.plugin.portal.useradmin;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apromore.dao.model.Group;
import org.apromore.dao.model.Role;
import org.apromore.dao.model.User;
import org.apromore.portal.model.UserType;
import org.apromore.plugin.portal.PortalContext;
import org.apromore.service.SecurityService;
import org.apromore.security.util.SecurityUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;;
//import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.zkoss.spring.SpringUtil;
import org.zkoss.json.JSONObject;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModels;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import org.apromore.plugin.portal.useradmin.listbox.*;
import org.apromore.plugin.portal.useradmin.listbox.TristateModel;

public class UserAdminController extends SelectorComposer<Window> {

    private static Logger LOGGER = LoggerFactory.getLogger(UserAdminController.class);
    private Map<String, String> roleMap = new HashMap<String, String>() {
        {
            put("ROLE_USER", "User");
            put("ROLE_ADMIN", "Administrator");
            put("ROLE_MANAGER", "Manager");
            put("ROLE_ANALYST", "Analyst");
            put("ROLE_OBSERVER", "Observer");
            put("ROLE_DESIGNER", "Designer");
            put("ROLE_DATA_SCIENTIST", "Data Scientist");
            put("ROLE_OPERATIONS", "Operations");
        }
    };

    private Comparator userComparator = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            String input = (String) o1;
            User user = (User) o2;
            return user.getUsername().contains(input) ? 0 : 1;
        }
    };

    public static Comparator<User> nameComparator = new Comparator<User>() {

        public int compare(User user1, User user2) {
            String username1 = user1.getUsername().toUpperCase();
            String username2 = user2.getUsername().toUpperCase();

            return username1.compareTo(username2);
        }};

    Window mainWindow;
    User currentUser;
    User selectedUser;
    Group selectedGroup;
    Set<User> selectedUsers;

    ListModelList<Group> groupModel;
    ListModelList<TristateModel> assignedRoleModel;
    ListModelList<TristateModel> assignedGroupModel;

    ListModelList<User> userModel;
    ListModelList<User> candidateUserModel;
    ListModelList<User> allUserModel;
    ListModelList<User> nonAssignedUserModel;
    ListModelList<User> assignedUserModel;

    UserListbox userList;
    GroupListbox groupList;
    AssignedUserListbox nonAssignedUserList;
    AssignedUserListbox assignedUserList;
    TristateListbox<Role> assignedRoleList;
    TristateListbox<Group> assignedGroupList;

    TristateItemRenderer assignedRoleItemRenderer;
    TristateItemRenderer assignedGroupItemRenderer;

    boolean isUserDetailDirty = false;
    boolean isGroupDetailDirty = false;

    boolean canViewUsers;
    boolean canEditUsers;
    boolean canEditGroups;
    boolean canEditRoles;

    private PortalContext portalContext = (PortalContext) Executions.getCurrent().getArg().get("portalContext");
    private SecurityService securityService = (SecurityService) /*SpringUtil.getBean("securityService");*/ Executions.getCurrent().getArg().get("securityService");

    @Wire("#groupsTab")
    Tab groupsTab;
    @Wire("#userListView")
    Vbox userListView;

    @Wire("#userListbox")
    Listbox userListbox;
    @Wire("#groupListbox")
    Listbox groupListbox;

    @Wire("#userDetail")
    Label userDetail;
    @Wire("#firstNameTextbox")
    Textbox firstNameTextbox;
    @Wire("#lastNameTextbox")
    Textbox lastNameTextbox;
    @Wire("#passwordTextbox")
    Textbox passwordTextbox;
    @Wire("#confirmPasswordTextbox")
    Textbox confirmPasswordTextbox;
    @Wire("#dateCreatedDatebox")
    Datebox dateCreatedDatebox;
    @Wire("#lastActivityDatebox")
    Datebox lastActivityDatebox;
    @Wire("#emailTextbox")
    Textbox emailTextbox;

    @Wire("#assignedRoleListbox")
    Listbox assignedRoleListbox;
    @Wire("#assignedGroupListbox")
    Listbox assignedGroupListbox;

    @Wire("#userSaveBtn")
    Button userSaveBtn;

    @Wire("#groupDetail")
    Label groupDetail;
    @Wire("#groupNameTextbox")
    Textbox groupNameTextbox;
    @Wire("#candidateUser")
    Combobox candidateUser;
    @Wire("#candidateUserAdd")
    Button candidateUserAdd;
    @Wire("#candidateUserRemove")
    Button candidateUserRemove;
    @Wire("#assignedUserAddBtn")
    Button assignedUserAddBtn;
    @Wire("#assignedUserRemoveBtn")
    Button assignedUserRemoveBtn;

    @Wire("#assignedUserAddView")
    Div assignedUserAddView;
    @Wire("#nonAssignedUserListbox")
    Listbox nonAssignedUserListbox;
    @Wire("#assignedUserListbox")
    Listbox assignedUserListbox;

    @Wire("#userAddBtn")
    Button userAddBtn;
    @Wire("#userEditBtn")
    Button userEditBtn;
    @Wire("#userRemoveBtn")
    Button userRemoveBtn;
    @Wire("#groupAddBtn")
    Button groupAddBtn;
    @Wire("#groupEditBtn")
    Button groupEditBtn;
    @Wire("#groupRemoveBtn")
    Button groupRemoveBtn;
    @Wire("#groupSaveBtn")
    Button groupSaveBtn;


    /**
     * Test whether the current user has a permission.
     *
     * @param permission any permission
     * @return whether the authenticated user has the <var>permission</var>
     */
    private boolean hasPermission(Permissions permission) {
        return securityService.hasAccess(portalContext.getCurrentUser().getId(), permission.getRowGuid());
    }

    @Override
    public void doAfterCompose(Window win) throws Exception {
        super.doAfterCompose(win);
        mainWindow = win;
        String userName = portalContext.getCurrentUser().getUsername();
        currentUser = securityService.getUserByName(userName);
        selectedUser = currentUser;

        canViewUsers = hasPermission(Permissions.VIEW_USERS);
        canEditUsers = hasPermission(Permissions.EDIT_USERS);
        canEditGroups = hasPermission(Permissions.EDIT_GROUPS);
        canEditRoles = hasPermission(Permissions.EDIT_ROLES);

        // Users tab
        userModel = new ListModelList<>(securityService.getAllUsers(), false);
        userModel.setMultiple(true);
        userList = new UserListbox(userListbox, userModel, "User name");

        refreshAssignedRoles();
        refreshAssignedGroups();

        firstNameTextbox.setReadonly(!canEditUsers);
        lastNameTextbox.setReadonly(!canEditUsers);
        emailTextbox.setReadonly(!canEditUsers);
        passwordTextbox.setReadonly(!canEditUsers);
        confirmPasswordTextbox.setReadonly(!canEditUsers);
        userAddBtn.setVisible(canEditUsers);
        userRemoveBtn.setVisible(canEditUsers);

        // Groups tab

        groupModel = new ListModelList<>(securityService.findElectiveGroups(), false);
        groupModel.setMultiple(true);
        groupList = new GroupListbox(groupListbox, groupModel, "Group name");

        allUserModel = new ListModelList<User>(securityService.getAllUsers(), false);
        // refreshCandidateUsers();
        refreshNonAssignedUsers();

        groupNameTextbox.setReadonly(!canEditGroups);
        candidateUser.setReadonly(!canEditGroups);
        candidateUserAdd.setDisabled(!canEditGroups);
        // candidateUserRemove.setDisabled(!canEditGroups);
        groupAddBtn.setVisible(canEditGroups);
        groupRemoveBtn.setVisible(canEditGroups);

        if (canViewUsers) {
            userListbox.setVisible(true);
            /*
            userEditBtn.addEventListener("onExecute", new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    String userName = event.getData().toString();
                    setSelectedUser(securityService.getUserByName(userName));
                }
            });*/
        } else {
            userListbox.setVisible(false);
        }

        if (!canEditUsers) {
            userListView.setVisible(false);
        }

        // Set default to nothing
        setSelectedUsers(null);
        setSelectedGroup(null);

        /*
        groupEditBtn.addEventListener("onExecute", new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                String groupName = event.getData().toString();
                setSelectedGroup(securityService.getGroupByName(groupName));
            }
        });
        groupEditBtn.addEventListener("onChangeNameCancel", new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                JSONObject param = (JSONObject) event.getData();
                String groupName = (String)param.get("groupName");
                String rowGuid = (String)param.get("rowGuid");
                Group group = securityService.getGroupByName(groupName);
                Textbox textbox = (Textbox)mainWindow.getFellow(rowGuid);
                textbox.setValue(groupName);
            }
        });
        groupEditBtn.addEventListener("onChangeNameOK", new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (!hasPermission(Permissions.EDIT_GROUPS)) {
                    Messagebox.show("You do not have permission to edit group", "Apromore", Messagebox.OK, Messagebox.ERROR);
                    return;
                }
                JSONObject param = (JSONObject) event.getData();
                String groupName = (String)param.get("groupName");
                String rowGuid = (String)param.get("rowGuid");
                Group group = securityService.getGroupByName(groupName);
                Textbox textbox = (Textbox)mainWindow.getFellow(rowGuid);
                if ("".equals(textbox.getValue())) {
                    securityService.deleteGroup(group);
                    groupModel.remove(group);
                    showNotification("Group " + group.getName() + " is deleted", "info");
                } else {
                    group.setName(textbox.getValue());
                    securityService.updateGroup(group);
                    showNotification("Details for group " + group.getName() + " is updated", "info");
                }
                refreshGroups();
                refreshAssignedGroups();
            }
        });
        */

        // Register ZK event handler
        EventQueue securityEventQueue = EventQueues.lookup(SecurityService.EVENT_TOPIC, getSelf().getDesktop().getWebApp(), true);
        securityEventQueue.subscribe(new EventListener() {
            @Override
            public void onEvent(Event event) {
                if (getSelf().getDesktop() == null) {
                    securityEventQueue.unsubscribe(this);
                } else {
                    Map properties = (Map) event.getData();
                    String eventType = (String) properties.get("type");
                    String eventUserName = (String) properties.get("user.name");

                    // Update the user collection
                    if (eventType.equals("CREATE_USER") || eventType.equals("DELETE_USER")) {
                        refreshUsers();
                        refreshCandidateUsers();
                    }

                    // Update the group collection
                    if (eventType.equals("CREATE_GROUP") || eventType.equals("DELETE_GROUP")) {
                        refreshGroups();
                        refreshAssignedGroups();
                    }

                    // Update the user panel
                    if ("UPDATE_USER".equals(eventType)) {
                        // TO DO: Check for dirty group detail
                        // Reset group panel
                        setSelectedGroup(null);
                    } if ("UPDATE_GROUP".equals(eventType)) {
                        refreshAssignedGroups();
                        if (selectedUser != null) {
                            String selectedUsername = selectedUser.getUsername();
                            // Skip this update if it doesn't apply to the currently displayed user
                            if (eventUserName != null && !eventUserName.equals(selectedUsername)) {
                                return;
                            }
                            // TO DO: Check for dirty user detail
                            setSelectedUsers(selectedUsers); // reload the current user
                        }
                    }



                }
            }
        });

        // Register OSGi event handler
        BundleContext bundleContext = (BundleContext) getSelf().getDesktop().getWebApp().getServletContext().getAttribute("osgi-bundlecontext");
        String filter = "(" + EventConstants.EVENT_TOPIC + "=" + SecurityService.EVENT_TOPIC + ")";
        Collection<ServiceReference> forwarders = bundleContext.getServiceReferences(EventHandler.class, filter);
        if (forwarders.isEmpty()) {
            Dictionary<String, Object> properties = new Hashtable<>();
            properties.put(EventConstants.EVENT_TOPIC, SecurityService.EVENT_TOPIC);
            bundleContext.registerService(EventHandler.class.getName(), new EventHandler() {
                @Override
                public final void handleEvent(org.osgi.service.event.Event event) {
                    Map<String, Object> properties = new HashMap<>();
                    for (String propertyName : event.getPropertyNames()) {
                        properties.put(propertyName, event.getProperty(propertyName));
                    }
                    securityEventQueue.publish(new Event(event.getTopic(), null, properties));
                }
            }, properties);
        }
    }

    private void showNotification(String message, String type) {
        Clients.evalJavaScript("Ap.common.notify('" + message + "','" + type + "');");
    }

    private void refreshUsers() {
        userModel = new ListModelList<>(securityService.getAllUsers(), false);
        userList.setSourceListModel(userModel);
        userList.reset();
        setSelectedUsers(null);
    }

    private void refreshCandidateUsers() {
        candidateUserModel = new ListModelList<>(securityService.getAllUsers(), false);
        candidateUserModel.setMultiple(true);
        candidateUser.setModel(ListModels.toListSubModel(candidateUserModel, userComparator, 20));
    }

    private void refreshNonAssignedUsers() {
        nonAssignedUserModel = new ListModelList<>(securityService.getAllUsers(), false);
        nonAssignedUserModel.setMultiple(true);
        nonAssignedUserList = new AssignedUserListbox(nonAssignedUserListbox, nonAssignedUserModel, "Users not in the group");
    }

    private void refreshGroups() {
        groupModel = new ListModelList<>(securityService.findElectiveGroups(), false);
        groupList.setSourceListModel(groupModel);
        groupList.reset();
    }

    private void refreshAssignedRoles() {
        Comparator<Role> compareRole = new Comparator<Role>() {
            @Override
            public int compare(Role el1, Role el2) {
                return el1.getName().compareTo(el2.getName());
            }
        };
        List<Role> roles = securityService.getAllRoles();
        Collections.sort(roles, compareRole);

        assignedRoleModel = new ListModelList<>();
        for (int i = 0; i < roles.size(); i++) {
            Role role = roles.get(i);
            String roleName = role.getName();
            assignedRoleModel.add(new TristateModel(roleMap.get(roleName), roleName, role, TristateModel.UNCHECKED));
        }
        assignedRoleModel.setMultiple(true);
        assignedRoleListbox.setModel(assignedRoleModel);
        assignedRoleListbox.setNonselectableTags("*");
        assignedRoleItemRenderer = new TristateItemRenderer();
        assignedRoleListbox.setItemRenderer(assignedRoleItemRenderer);
        assignedRoleList = new TristateListbox<Role>(assignedRoleListbox, assignedRoleModel, "Assigned Roles");
        assignedRoleItemRenderer.setList(assignedRoleList);

    }

    private void refreshAssignedGroups() {
        Comparator<Group> compareGroup = new Comparator<Group>() {
            @Override
            public int compare(Group el1, Group el2) {
                return el1.getName().compareTo(el2.getName());
            }
        };
        List<Group> groups = securityService.findElectiveGroups();
        Collections.sort(groups, compareGroup);

        assignedGroupModel = new ListModelList<>();
        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            String groupName = group.getName();
            assignedGroupModel.add(new TristateModel(groupName, groupName, group, TristateModel.UNCHECKED));
        }
        assignedGroupModel.setMultiple(true);
        assignedGroupListbox.setModel(assignedGroupModel);
        assignedGroupListbox.setNonselectableTags("*");
        assignedGroupItemRenderer = new TristateItemRenderer();
        assignedGroupListbox.setItemRenderer(assignedGroupItemRenderer);
        assignedGroupList = new TristateListbox<Group>(assignedGroupListbox, assignedGroupModel, "Assigned Groups");
        assignedGroupItemRenderer.setList(assignedGroupList);
    }

    private void updateTristateModels(TristateListbox list, Map<String, Integer> tally, Integer total) {
        Map<String, Integer> keyToIndexMap = list.getKeyToIndexMap();
        for (Map.Entry<String, Integer> entry : keyToIndexMap.entrySet()) {
            String key = entry.getKey();
            int index = (int)entry.getValue();
            Integer count = tally.get(key);
            Integer state;
            boolean twoStateOnly = false;
            if (count == null) { // no entry
                state = TristateModel.UNCHECKED;
                twoStateOnly = true;
            } else if (count < total) {
                state = TristateModel.INDETERMINATE;
            } else {
                state = TristateModel.CHECKED;
                twoStateOnly = true;
            }
            ListModelList<TristateModel> listModel = list.getListModel();
            TristateModel model = listModel.get(index);
            model.setState(state);
            model.setTwoStateOnly(twoStateOnly);
            listModel.set(index, model); // trigger change
        }
    }

    private void clearAssignedRoleModel() {
        assignedRoleList.reset();
    }

    private void updateAssignedRoleModel(Set<User> users) {
        if (users == null) {
            clearAssignedRoleModel();
            return;
        }
        Map<String, Integer> tally = new HashMap<String, Integer>();
        for (User user: users) {
            Set<Role> roles = securityService.findRolesByUser(user);
            for (Role role: roles) {
                String roleName = role.getName();
                Integer state = tally.get(roleName);
                if (state != null) {
                    tally.put(roleName, state + 1);
                } else {
                    tally.put(roleName, 1);
                }
            }
        }
        int userCount = users.size();
        updateTristateModels(assignedRoleList, tally, userCount);
    }

    private void saveAssignedRole(Set<User> users, boolean persist) {
        if (users == null) {
            return;
        }
        assignedRoleList.calcSelection();
        Set<Role> addedRoles = assignedRoleList.getAddedObjects();
        Set<Role> removedRoles = assignedRoleList.getRemovedObjects();
        for (User user: users) {
            Set<Role> roles = securityService.findRolesByUser(user);
            Set<Role> updatedRoles = new HashSet<Role>(roles);
            updatedRoles.addAll(addedRoles);
            updatedRoles.removeAll(removedRoles);
            user.setRoles(updatedRoles);
            if (persist) {
                securityService.updateUser(user);
            }
        }
    }

    private void clearAssignedGroupModel() {
        assignedGroupList.reset();
    }

    private void updateAssignedGroupModel(Set<User> users) {
        if (users == null) {
            clearAssignedGroupModel();
            return;
        }
        Map<String, Integer> tally = new HashMap<String, Integer>();
        for (User user: users) {
            Set<Group> groups = securityService.findGroupsByUser(user);
            for (Group group: groups) {
                String groupName = group.getName();
                Integer state = tally.get(groupName);
                if (state != null) {
                    tally.put(groupName, state + 1);
                } else {
                    tally.put(groupName, 1);
                }
            }
        }
        int userCount = users.size();
        updateTristateModels(assignedGroupList, tally, userCount);
    }

    private void saveAssignedGroup(Set<User> users, boolean persist) {
        if (users == null) {
            return;
        }
        assignedGroupList.calcSelection();
        Set<Group> addedGroups = assignedGroupList.getAddedObjects();
        Set<Group> removedGroups = assignedGroupList.getRemovedObjects();
        for (User user: users) {
            Set<Group> roles = securityService.findGroupsByUser(user);
            Set<Group> updatedGroups = new HashSet<Group>(roles);
            updatedGroups.addAll(addedGroups);
            updatedGroups.removeAll(removedGroups);
            user.setGroups(updatedGroups);
            if (persist) {
                securityService.updateUser(user);
            }
        }
    }

    private void setSelectedUsers(Set<User> users) {
        isUserDetailDirty = false;
        passwordTextbox.setValue("");
        confirmPasswordTextbox.setValue("");
        assignedRoleItemRenderer.setDisabled(false);
        assignedGroupItemRenderer.setDisabled(false);
        assignedRoleList.reset();
        assignedGroupList.reset();
        assignedRoleListbox.setDisabled(false);
        assignedGroupListbox.setDisabled(false);
        if (users == null || users.size() == 0 || users.size() > 1) {
            selectedUser = null;
            selectedUsers = users;
            firstNameTextbox.setValue("");
            lastNameTextbox.setValue("");
            dateCreatedDatebox.setValue(null);
            lastActivityDatebox.setValue(null);
            emailTextbox.setValue("");
            if (users == null) {
                userDetail.setValue("No user is selected");
                userSaveBtn.setDisabled(true);
            } else {
                userDetail.setValue("Multiple users are selected");
                userSaveBtn.setDisabled(false);
            }
            if (users == null || users.size() == 0) {
                assignedRoleItemRenderer.setDisabled(true);
                assignedGroupItemRenderer.setDisabled(true);
                assignedRoleListbox.setDisabled(true);
                assignedGroupListbox.setDisabled(true);
            } else {
                assignedRoleItemRenderer.setForceTwoState(false);
                assignedGroupItemRenderer.setForceTwoState(false);
            }
        } else {
            selectedUser = users.iterator().next();
            selectedUsers = users;
            firstNameTextbox.setValue(selectedUser.getFirstName());
            lastNameTextbox.setValue(selectedUser.getLastName());
            dateCreatedDatebox.setValue(selectedUser.getDateCreated());
            lastActivityDatebox.setValue(selectedUser.getLastActivityDate());
            emailTextbox.setValue(selectedUser.getMembership().getEmail());
            userDetail.setValue("User: " + selectedUser.getUsername());
            userSaveBtn.setDisabled(false);
            assignedRoleItemRenderer.setForceTwoState(true);
            assignedGroupItemRenderer.setForceTwoState(true);
        }
        updateAssignedRoleModel(users);
        updateAssignedGroupModel(users);
    }

    private Group setSelectedGroup(Group group) {
        isGroupDetailDirty = false;
        assignedUserAddView.setVisible(false);
        if (group == null) {
            groupNameTextbox.setValue("");
            groupDetail.setValue("");
            assignedUserModel = new ListModelList<>();
            nonAssignedUserModel = new ListModelList<>();
            groupSaveBtn.setDisabled(true);
        } else {
            groupNameTextbox.setValue(group.getName());
            groupDetail.setValue("Group: " + group.getName());
            List<User> assignedUsers = new ArrayList<>(group.getUsers());
            List<User> nonAssignedUsers = new ArrayList<>(securityService.getAllUsers());
            nonAssignedUsers.removeAll(assignedUsers);
            Collections.sort(assignedUsers, nameComparator);
            Collections.sort(nonAssignedUsers, nameComparator);
            assignedUserModel = new ListModelList<User>(assignedUsers, false);
            nonAssignedUserModel = new ListModelList<User>(nonAssignedUsers, false);
            groupSaveBtn.setDisabled(false);
        }
        assignedUserModel.setMultiple(true);
        assignedUserList = new AssignedUserListbox(assignedUserListbox, assignedUserModel, "Assigned Users");
        nonAssignedUserModel.setMultiple(true);
        nonAssignedUserList = new AssignedUserListbox(nonAssignedUserListbox, nonAssignedUserModel, "Users not in the group");
        // candidateUserModel.clearSelection();
        selectedGroup = group;
        return group;
    }

    /*
        Find the User from ListModelList based on the set
     */
    private Set<User> getUserCollection(ListModelList model, Set<User> userSet) {
        Set<String> userNames = new HashSet<String>();
        Set<User> users = new HashSet<User>();
        for (User u : userSet) {
            userNames.add(u.getUsername());
        }
        for (int i = 0; i < model.size(); i++) {
            User user = (User) model.get(i);
            if (userNames.contains(user.getUsername())) {
                users.add(user);
            }
        }
        return users;
    }

    // User-related features

    @Listen("onSelect = #userListbox")
    public void onSelectUserListbox(SelectEvent event) throws Exception {
        if (!hasPermission(Permissions.VIEW_USERS)) {
            throw new Exception("Cannot view users without permission");
        }
        Set<User> users = event.getSelectedObjects();
        if (users.size() == 1) {
            // User user = selectedUsers.iterator().next();
            // setSelectedUser(securityService.getUserByName(user.getUsername()));
            Set<User> prevUsers = event.getPreviousSelectedObjects();
            if (prevUsers.equals(users)) {
                userListbox.clearSelection();
                setSelectedUsers(null);
            } else {
                setSelectedUsers(users);
            }
        } else if (users.size() > 1) {
            setSelectedUsers(users);
        } else {
            setSelectedUsers(null);
        }
    }

    @Listen("onSelect = #assignedGroupListbox")
    public void onSelectAssignedGroupsListbox(SelectEvent event) {
        if (!hasPermission(Permissions.EDIT_GROUPS)) {
            groupListbox.setSelectedItems(event.getPreviousSelectedItems());
            Messagebox.show("You do not have permission to assign group(s)", "Apromore", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        // selectedUser.setGroups(event.getSelectedObjects());
        // securityService.updateUser(selectedUser);
        // showNotification("Groups for user " + selectedUser.getUsername() + " is updated", "info");
    }

    @Listen("onSelect = #assignedRoleListbox")
    public void onSelectAssignedRolesListbox(SelectEvent event) {
        if (!hasPermission(Permissions.EDIT_ROLES)) {
            assignedRoleListbox.setSelectedItems(event.getPreviousSelectedItems());
            Messagebox.show("You do not have permission to assign roles", "Apromore", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        // selectedUser.setRoles(event.getSelectedObjects());
        // securityService.updateUser(selectedUser);
        // showNotification("Roles for user " + selectedUser.getUsername() + " is updated", "info");
    }

    @Listen("onClick = #userAddBtn")
    public void onClickuserAddBtn() {
        if (!hasPermission(Permissions.EDIT_USERS)) {
            Messagebox.show("You do not have permission to add user", "Apromore", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        try {
            Map arg = new HashMap<>();
            arg.put("portalContext", portalContext);
            arg.put("securityService", securityService);
            Window window = (Window) Executions.getCurrent().createComponents("user-admin/zul/create-user.zul", getSelf(), arg);
            window.doModal();

        } catch (Exception e) {
            LOGGER.error("Unable to create user creation dialog", e);
            Messagebox.show("Unable to create user creation dialog");
        }
    }

    @Listen("onClick = #userRemoveBtn")
    public void onClickUserRemoveBtn() {
        if (!hasPermission(Permissions.EDIT_USERS)) {
            Messagebox.show("You do not have permission to delete user", "Apromore", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        Set<User> selectedUsers = userList.getSelection();
        if (selectedUsers.size() == 0) {
            Messagebox.show("Nothing to delete", "Apromore", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (selectedUsers.contains(currentUser)) {
            Messagebox.show("You can not delete your own account", "Apromore", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        List<String> users = new ArrayList<>();
        for (User u : selectedUsers) {
            users.add(u.getUsername());
        }
        String userNames = String.join(",", users);
        Messagebox.show("Do you really want to delete " + userNames + "?",
                "Question",
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener() {
                    public void onEvent(Event e) {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            for (User user : selectedUsers) {
                                LOGGER.info("Deleting user " + user.getUsername());
                                securityService.deleteUser(user);
                            }
                            setSelectedUsers(null);
                        }
                    }
                }
        );
    }

    @Listen("onClick = #userSaveBtn")
    public void onClickUserSaveButton() {
        boolean passwordDirty = false;

        if (!hasPermission(Permissions.EDIT_USERS)) {
            Messagebox.show("You do not have permission to edit user", "Apromore", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (selectedUser != null) {
            if (passwordTextbox.getValue() != null && passwordTextbox.getValue().length() > 0) {
                if (passwordTextbox.getValue().length() < 6) {
                    Messagebox.show("New password must be at least 6 characters long.", null, Messagebox.OK, Messagebox.ERROR);
                    return;
                } else if (!Objects.equals(passwordTextbox.getValue(), confirmPasswordTextbox.getValue())) {
                    Messagebox.show("Password does not match.", null, Messagebox.OK, Messagebox.ERROR);
                    return;
                }
                passwordDirty = true;
            }

            selectedUser.setFirstName(firstNameTextbox.getValue());
            selectedUser.setLastName(lastNameTextbox.getValue());
            saveAssignedGroup(selectedUsers, false);
            saveAssignedRole(selectedUsers, false);
            selectedUser.getMembership().setEmail(emailTextbox.getValue());
            if (passwordDirty) {
                selectedUser.getMembership().setPassword(SecurityUtil.hashPassword(passwordTextbox.getValue()));
                selectedUser.getMembership().setSalt("username");
            }
            selectedUser.getMembership().setUser(selectedUser);
            securityService.updateUser(selectedUser);
            showNotification("Details for user " + selectedUser.getUsername() + " is updated", "info");
        } else {
            saveAssignedGroup(selectedUsers, true);
            saveAssignedRole(selectedUsers, true);
            showNotification("Roles and groups for multiple users are updated", "info");
        }
        isUserDetailDirty = false;
    }

    // Group-related features

    @Listen("onSelect = #groupListbox")
    public void onSelectGroupsListbox(SelectEvent event) {
        if (!hasPermission(Permissions.EDIT_GROUPS)) {
            Messagebox.show("You do not have permission to edit group", "Apromore", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        Set<Group> selectedGroups = event.getSelectedObjects();
        if (selectedGroups.size() == 1) {
            Group group = selectedGroups.iterator().next();
            setSelectedGroup(securityService.getGroupByName(group.getName()));
        } else {
            setSelectedGroup(null);
        }
    }

    @Listen("onClick = #assignedUserAddBtn")
    public void onClickAssignedUserAdd() {
        if (!hasPermission(Permissions.EDIT_GROUPS)) {
            Messagebox.show("You do not have permission to allocate users to a group", "Apromore", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (assignedUserAddView.isVisible()) {
            assignedUserAddView.setVisible(false);
        } else {
            assignedUserAddView.setVisible(true);
        }
    }

    @Listen("onClick = #assignedUserRemoveBtn")
    public void onClickAssignedUserRemove() {
        if (!hasPermission(Permissions.EDIT_GROUPS)) {
            Messagebox.show("You do not have permission to allocate users to a group", "Apromore", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        ListModelList listModel = assignedUserList.getListModel();
        Set<User> users = listModel.getSelection();
        listModel.removeAll(users);
        assignedUserModel.remove(users);
    }

    @Listen("onClick = #candidateUserAdd")
    public void onClickCandidateUserAdd() {
        if (!hasPermission(Permissions.EDIT_GROUPS)) {
            Messagebox.show("You do not have permission to allocate users to a group", "Apromore", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        Set<User> users = candidateUserModel.getSelection();
        if (users != null && users.size() == 1) {
            User candidateUser = users.iterator().next();
            // Change to Set.add
            for (int i = 0; i < assignedUserModel.size(); i++) {
                User user = (User) assignedUserModel.get(i);
                if (candidateUser.getUsername().contains(user.getUsername())) {
                    return;
                }
            }
            assignedUserModel.add(candidateUser);
            assignedUserList.getListModel().add(candidateUser);
        }
    }

    @Listen("onClick = #retractUser")
    public void onClickRetractUser() {
        if (selectedGroup == null) {
            return;
        }
        if (!hasPermission(Permissions.EDIT_GROUPS)) {
            Messagebox.show("You do not have permission to allocate users to a group", "Apromore", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        List<User> users = new ArrayList<User>(assignedUserList.getSelection());
        if (users != null && users.size() >= 1) {
            for(User user: users) {
                nonAssignedUserModel.add(user);
                // nonAssignedUserList.getListModel().add(user);
                assignedUserModel.remove(user);
                // assignedUserList.getListModel().remove(user);
                nonAssignedUserList.reset();
                assignedUserList.reset();
            }
        }
    }

    @Listen("onClick = #assignUser")
    public void onClickAssignUser() {
        if (selectedGroup == null) {
            return;
        }
        if (!hasPermission(Permissions.EDIT_GROUPS)) {
            Messagebox.show("You do not have permission to allocate users to a group", "Apromore", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        List<User> users = new ArrayList<User>(nonAssignedUserList.getSelection());
        if (users != null && users.size() >= 1) {
            for(User user: users) {
                assignedUserModel.add(user);
                // assignedUserList.getListModel().add(user);
                nonAssignedUserModel.remove(user);
                // nonAssignedUserList.getListModel().remove(user);
                nonAssignedUserList.reset();
                assignedUserList.reset();
            }
        }
    }

    @Listen("onClick = #groupAddBtn")
    public void onClickgroupAddBtn() {
        if (!hasPermission(Permissions.EDIT_GROUPS)) {
            Messagebox.show("You do not have permission to create group", "Apromore", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        try {
            Map arg = new HashMap<>();
            arg.put("portalContext", portalContext);
            arg.put("securityService", securityService);
            Window window = (Window) Executions.getCurrent().createComponents("user-admin/zul/create-group.zul", getSelf(), arg);
            window.doModal();

        } catch (Exception e) {
            LOGGER.error("Unable to create group creation dialog", e);
            Messagebox.show("Unable to create group creation dialog");
        }
    }

    @Listen("onClick = #groupRemoveBtn")
    public void onClickGroupRemoveBtn() {
        if (!hasPermission(Permissions.EDIT_GROUPS)) {
            Messagebox.show("You do not have permission to delete group", "Apromore", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        Set<Group> selectedGroups = groupList.getSelection();
        if (selectedGroups.size() == 0) {
            Messagebox.show("Nothing to delete", "Apromore", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
        }

        List<String> groups = new ArrayList<>();
        for (Group g : selectedGroups) {
            groups.add(g.getName());
        }
        String groupNames = String.join(",", groups);
        Messagebox.show("Do you really want to delete " + groupNames + "?",
                "Question",
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener() {
                    public void onEvent(Event e) {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            for (Group group : selectedGroups) {
                                LOGGER.info("Deleting user " + group.getName());
                                securityService.deleteGroup(group);
                            }
                            setSelectedGroup(null);
                        }
                    }
                }
        );
    }

    @Listen("onClick = #groupSaveBtn")
    public void onClickGroupSaveButton() {
        if (!hasPermission(Permissions.EDIT_GROUPS)) {
            Messagebox.show("You do not have permission to edit groups", "Apromore", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        ListModelList listModel = assignedUserList.getListModel();
        Set<User> users = new HashSet<User>(listModel);
        selectedGroup.setName(groupNameTextbox.getValue());
        selectedGroup.setUsers(users);
        securityService.updateGroup(selectedGroup);
        showNotification("Details for group " + selectedGroup.getName() + " is updated", "info");
        isGroupDetailDirty = false;
        refreshGroups();
        setSelectedGroup(null);
    }

    @Listen("onClick = #userSelectAllBtn")
    public void onUserSelectAllBtn() {
        userList.selectAll();
        setSelectedUsers(null);
    }

    @Listen("onClick = #userSelectNoneBtn")
    public void onUserSelectNoneBtn() {
        userList.unselectAll();
        setSelectedUsers(null);
    }

    @Listen("onClick = #groupSelectAllBtn")
    public void onGroupSelectAllBtn() {
        groupList.selectAll();
        setSelectedGroup(null);
    }

    @Listen("onClick = #groupSelectNoneBtn")
    public void onGroupSelectNoneBtn() {
        groupList.unselectAll();
        setSelectedGroup(null);
    }

    @Listen("onClick = #okBtn")
    public void onClickOkButton() {
        getSelf().detach();
    }

    @Listen("onChanging = #firstNameTextbox")
    public void onChangingFirstName() {
        isUserDetailDirty = true;
    }            

    @Listen("onChanging = #lastNameTextbox")
    public void onChangingLastName() {
        isUserDetailDirty = true;
    }

    @Listen("onChanging = #passwordTextbox")
    public void onChangingPassword() {
        isUserDetailDirty = true;
    }

    @Listen("onChanging = #confirmPasswordTextbox")
    public void onChangingConfirmPassword() {
        isUserDetailDirty = true;
    }

    @Listen("onChanging = #dateCreatedDatebox")
    public void onChangingCreatedDatebox() {
        isUserDetailDirty = true;
    }

    @Listen("onChanging = #lastActivityDatebox")
    public void onChangingLastActivityDatebox() {
        isUserDetailDirty = true;
    }

    @Listen("onChanging = #emailTextbox")
    public void onChangingEmail() {
        isUserDetailDirty = true;
    }

    @Listen("onChanging = #groupNameTextbox")
    public void onChangingGroupNameTextbox() {
        isGroupDetailDirty = true;
    }

    // Scheduled for removal

    /*
    @Listen("onOK = #groupListbox")
    public void onOKGroupsListbox(KeyEvent event) {
        if (!hasPermission(Permissions.EDIT_GROUPS)) {
            Messagebox.show("You do not have permission to edit group", "Apromore", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        Textbox textbox = (Textbox) event.getReference();
        Group group = securityService.findGroupByRowGuid(textbox.getId());
        if ("".equals(textbox.getValue())) {
            securityService.deleteGroup(group);
            groupModel.remove(group);

        } else {
            group.setName(textbox.getValue());
            securityService.updateGroup(group);
        }
    }
    */


}
