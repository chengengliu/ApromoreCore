
package org.wfmc._2002.xpdl1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.wfmc._2002.xpdl1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Cost_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "Cost");
    private final static QName _Length_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "Length");
    private final static QName _Vendor_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "Vendor");
    private final static QName _Icon_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "Icon");
    private final static QName _Limit_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "Limit");
    private final static QName _Description_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "Description");
    private final static QName _Responsible_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "Responsible");
    private final static QName _ActualParameter_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "ActualParameter");
    private final static QName _CostUnit_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "CostUnit");
    private final static QName _WaitingTime_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "WaitingTime");
    private final static QName _Priority_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "Priority");
    private final static QName _InitialValue_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "InitialValue");
    private final static QName _Created_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "Created");
    private final static QName _Duration_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "Duration");
    private final static QName _ValidTo_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "ValidTo");
    private final static QName _WorkingTime_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "WorkingTime");
    private final static QName _Codepage_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "Codepage");
    private final static QName _Version_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "Version");
    private final static QName _ValidFrom_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "ValidFrom");
    private final static QName _Documentation_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "Documentation");
    private final static QName _XPDLVersion_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "XPDLVersion");
    private final static QName _Countrykey_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "Countrykey");
    private final static QName _Performer_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "Performer");
    private final static QName _Author_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "Author");
    private final static QName _PriorityUnit_QNAME = new QName("http://www.wfmc.org/2002/XPDL1.0", "PriorityUnit");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.wfmc._2002.xpdl1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ActivitySet }
     * 
     */
    public ActivitySet createActivitySet() {
        return new ActivitySet();
    }

    /**
     * Create an instance of {@link Xpression }
     * 
     */
    public Xpression createXpression() {
        return new Xpression();
    }

    /**
     * Create an instance of {@link TypeDeclarations }
     * 
     */
    public TypeDeclarations createTypeDeclarations() {
        return new TypeDeclarations();
    }

    /**
     * Create an instance of {@link ListType }
     * 
     */
    public ListType createListType() {
        return new ListType();
    }

    /**
     * Create an instance of {@link Activities }
     * 
     */
    public Activities createActivities() {
        return new Activities();
    }

    /**
     * Create an instance of {@link TransitionRefs }
     * 
     */
    public TransitionRefs createTransitionRefs() {
        return new TransitionRefs();
    }

    /**
     * Create an instance of {@link EnumerationValue }
     * 
     */
    public EnumerationValue createEnumerationValue() {
        return new EnumerationValue();
    }

    /**
     * Create an instance of {@link PackageHeader }
     * 
     */
    public PackageHeader createPackageHeader() {
        return new PackageHeader();
    }

    /**
     * Create an instance of {@link WorkflowProcess }
     * 
     */
    public WorkflowProcess createWorkflowProcess() {
        return new WorkflowProcess();
    }

    /**
     * Create an instance of {@link SubFlow }
     * 
     */
    public SubFlow createSubFlow() {
        return new SubFlow();
    }

    /**
     * Create an instance of {@link Automatic }
     * 
     */
    public Automatic createAutomatic() {
        return new Automatic();
    }

    /**
     * Create an instance of {@link Transition }
     * 
     */
    public Transition createTransition() {
        return new Transition();
    }

    /**
     * Create an instance of {@link ArrayType }
     * 
     */
    public ArrayType createArrayType() {
        return new ArrayType();
    }

    /**
     * Create an instance of {@link Activity }
     * 
     */
    public Activity createActivity() {
        return new Activity();
    }

    /**
     * Create an instance of {@link FormalParameters }
     * 
     */
    public FormalParameters createFormalParameters() {
        return new FormalParameters();
    }

    /**
     * Create an instance of {@link Applications }
     * 
     */
    public Applications createApplications() {
        return new Applications();
    }

    /**
     * Create an instance of {@link Split }
     * 
     */
    public Split createSplit() {
        return new Split();
    }

    /**
     * Create an instance of {@link SimulationInformation }
     * 
     */
    public SimulationInformation createSimulationInformation() {
        return new SimulationInformation();
    }

    /**
     * Create an instance of {@link FormalParameter }
     * 
     */
    public FormalParameter createFormalParameter() {
        return new FormalParameter();
    }

    /**
     * Create an instance of {@link Implementation }
     * 
     */
    public Implementation createImplementation() {
        return new Implementation();
    }

    /**
     * Create an instance of {@link DeclaredType }
     * 
     */
    public DeclaredType createDeclaredType() {
        return new DeclaredType();
    }

    /**
     * Create an instance of {@link ExternalPackage }
     * 
     */
    public ExternalPackage createExternalPackage() {
        return new ExternalPackage();
    }

    /**
     * Create an instance of {@link Application }
     * 
     */
    public Application createApplication() {
        return new Application();
    }

    /**
     * Create an instance of {@link ActivitySets }
     * 
     */
    public ActivitySets createActivitySets() {
        return new ActivitySets();
    }

    /**
     * Create an instance of {@link Tool }
     * 
     */
    public Tool createTool() {
        return new Tool();
    }

    /**
     * Create an instance of {@link Member }
     * 
     */
    public Member createMember() {
        return new Member();
    }

    /**
     * Create an instance of {@link Script }
     * 
     */
    public Script createScript() {
        return new Script();
    }

    /**
     * Create an instance of {@link Responsibles }
     * 
     */
    public Responsibles createResponsibles() {
        return new Responsibles();
    }

    /**
     * Create an instance of {@link Package }
     * 
     */
    public Package createPackage() {
        return new Package();
    }

    /**
     * Create an instance of {@link RedefinableHeader }
     * 
     */
    public RedefinableHeader createRedefinableHeader() {
        return new RedefinableHeader();
    }

    /**
     * Create an instance of {@link UnionType }
     * 
     */
    public UnionType createUnionType() {
        return new UnionType();
    }

    /**
     * Create an instance of {@link TypeDeclaration }
     * 
     */
    public TypeDeclaration createTypeDeclaration() {
        return new TypeDeclaration();
    }

    /**
     * Create an instance of {@link RecordType }
     * 
     */
    public RecordType createRecordType() {
        return new RecordType();
    }

    /**
     * Create an instance of {@link WorkflowProcesses }
     * 
     */
    public WorkflowProcesses createWorkflowProcesses() {
        return new WorkflowProcesses();
    }

    /**
     * Create an instance of {@link ParticipantType }
     * 
     */
    public ParticipantType createParticipantType() {
        return new ParticipantType();
    }

    /**
     * Create an instance of {@link DataFields }
     * 
     */
    public DataFields createDataFields() {
        return new DataFields();
    }

    /**
     * Create an instance of {@link EnumerationType }
     * 
     */
    public EnumerationType createEnumerationType() {
        return new EnumerationType();
    }

    /**
     * Create an instance of {@link ExternalReference }
     * 
     */
    public ExternalReference createExternalReference() {
        return new ExternalReference();
    }

    /**
     * Create an instance of {@link No }
     * 
     */
    public No createNo() {
        return new No();
    }

    /**
     * Create an instance of {@link Manual }
     * 
     */
    public Manual createManual() {
        return new Manual();
    }

    /**
     * Create an instance of {@link ExternalPackages }
     * 
     */
    public ExternalPackages createExternalPackages() {
        return new ExternalPackages();
    }

    /**
     * Create an instance of {@link Route }
     * 
     */
    public Route createRoute() {
        return new Route();
    }

    /**
     * Create an instance of {@link BasicType }
     * 
     */
    public BasicType createBasicType() {
        return new BasicType();
    }

    /**
     * Create an instance of {@link ProcessHeader }
     * 
     */
    public ProcessHeader createProcessHeader() {
        return new ProcessHeader();
    }

    /**
     * Create an instance of {@link TransitionRef }
     * 
     */
    public TransitionRef createTransitionRef() {
        return new TransitionRef();
    }

    /**
     * Create an instance of {@link TransitionRestriction }
     * 
     */
    public TransitionRestriction createTransitionRestriction() {
        return new TransitionRestriction();
    }

    /**
     * Create an instance of {@link FinishMode }
     * 
     */
    public FinishMode createFinishMode() {
        return new FinishMode();
    }

    /**
     * Create an instance of {@link StartMode }
     * 
     */
    public StartMode createStartMode() {
        return new StartMode();
    }

    /**
     * Create an instance of {@link TransitionRestrictions }
     * 
     */
    public TransitionRestrictions createTransitionRestrictions() {
        return new TransitionRestrictions();
    }

    /**
     * Create an instance of {@link BlockActivity }
     * 
     */
    public BlockActivity createBlockActivity() {
        return new BlockActivity();
    }

    /**
     * Create an instance of {@link Join }
     * 
     */
    public Join createJoin() {
        return new Join();
    }

    /**
     * Create an instance of {@link Deadline }
     * 
     */
    public Deadline createDeadline() {
        return new Deadline();
    }

    /**
     * Create an instance of {@link Condition }
     * 
     */
    public Condition createCondition() {
        return new Condition();
    }

    /**
     * Create an instance of {@link ExtendedAttribute }
     * 
     */
    public ExtendedAttribute createExtendedAttribute() {
        return new ExtendedAttribute();
    }

    /**
     * Create an instance of {@link Participant }
     * 
     */
    public Participant createParticipant() {
        return new Participant();
    }

    /**
     * Create an instance of {@link SchemaType }
     * 
     */
    public SchemaType createSchemaType() {
        return new SchemaType();
    }

    /**
     * Create an instance of {@link TimeEstimation }
     * 
     */
    public TimeEstimation createTimeEstimation() {
        return new TimeEstimation();
    }

    /**
     * Create an instance of {@link ConformanceClass }
     * 
     */
    public ConformanceClass createConformanceClass() {
        return new ConformanceClass();
    }

    /**
     * Create an instance of {@link ActualParameters }
     * 
     */
    public ActualParameters createActualParameters() {
        return new ActualParameters();
    }

    /**
     * Create an instance of {@link ExtendedAttributes }
     * 
     */
    public ExtendedAttributes createExtendedAttributes() {
        return new ExtendedAttributes();
    }

    /**
     * Create an instance of {@link DataField }
     * 
     */
    public DataField createDataField() {
        return new DataField();
    }

    /**
     * Create an instance of {@link DataType }
     * 
     */
    public DataType createDataType() {
        return new DataType();
    }

    /**
     * Create an instance of {@link Participants }
     * 
     */
    public Participants createParticipants() {
        return new Participants();
    }

    /**
     * Create an instance of {@link Transitions }
     * 
     */
    public Transitions createTransitions() {
        return new Transitions();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "Cost")
    public JAXBElement<String> createCost(String value) {
        return new JAXBElement<String>(_Cost_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "Length")
    public JAXBElement<String> createLength(String value) {
        return new JAXBElement<String>(_Length_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "Vendor")
    public JAXBElement<String> createVendor(String value) {
        return new JAXBElement<String>(_Vendor_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "Icon")
    public JAXBElement<String> createIcon(String value) {
        return new JAXBElement<String>(_Icon_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "Limit")
    public JAXBElement<String> createLimit(String value) {
        return new JAXBElement<String>(_Limit_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "Description")
    public JAXBElement<String> createDescription(String value) {
        return new JAXBElement<String>(_Description_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "Responsible")
    public JAXBElement<String> createResponsible(String value) {
        return new JAXBElement<String>(_Responsible_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "ActualParameter")
    public JAXBElement<String> createActualParameter(String value) {
        return new JAXBElement<String>(_ActualParameter_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "CostUnit")
    public JAXBElement<String> createCostUnit(String value) {
        return new JAXBElement<String>(_CostUnit_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "WaitingTime")
    public JAXBElement<String> createWaitingTime(String value) {
        return new JAXBElement<String>(_WaitingTime_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "Priority")
    public JAXBElement<String> createPriority(String value) {
        return new JAXBElement<String>(_Priority_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "InitialValue")
    public JAXBElement<String> createInitialValue(String value) {
        return new JAXBElement<String>(_InitialValue_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "Created")
    public JAXBElement<String> createCreated(String value) {
        return new JAXBElement<String>(_Created_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "Duration")
    public JAXBElement<String> createDuration(String value) {
        return new JAXBElement<String>(_Duration_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "ValidTo")
    public JAXBElement<String> createValidTo(String value) {
        return new JAXBElement<String>(_ValidTo_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "WorkingTime")
    public JAXBElement<String> createWorkingTime(String value) {
        return new JAXBElement<String>(_WorkingTime_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "Codepage")
    public JAXBElement<String> createCodepage(String value) {
        return new JAXBElement<String>(_Codepage_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "Version")
    public JAXBElement<String> createVersion(String value) {
        return new JAXBElement<String>(_Version_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "ValidFrom")
    public JAXBElement<String> createValidFrom(String value) {
        return new JAXBElement<String>(_ValidFrom_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "Documentation")
    public JAXBElement<String> createDocumentation(String value) {
        return new JAXBElement<String>(_Documentation_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "XPDLVersion")
    public JAXBElement<String> createXPDLVersion(String value) {
        return new JAXBElement<String>(_XPDLVersion_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "Countrykey")
    public JAXBElement<String> createCountrykey(String value) {
        return new JAXBElement<String>(_Countrykey_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "Performer")
    public JAXBElement<String> createPerformer(String value) {
        return new JAXBElement<String>(_Performer_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "Author")
    public JAXBElement<String> createAuthor(String value) {
        return new JAXBElement<String>(_Author_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.wfmc.org/2002/XPDL1.0", name = "PriorityUnit")
    public JAXBElement<String> createPriorityUnit(String value) {
        return new JAXBElement<String>(_PriorityUnit_QNAME, String.class, null, value);
    }

}
