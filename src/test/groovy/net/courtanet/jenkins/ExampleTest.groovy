import org.junit.*
import org.junit.rules.TemporaryFolder

import com.lesfurets.jenkins.unit.BasePipelineTest

class ExampleTest extends BasePipelineTest {

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder()

    static File temp

    @BeforeClass
    static void init() {
        temp = folder.newFolder('libs')
        System.setProperty("printstack.disabled", "false")
    }

    @Before
    void setup() {
        super.setUp()
        helper.registerAllowedMethod('sh', [Map]) { args ->
            println("sh was called with=" + args)
            return 'bcc19744'
        }
        helper.registerAllowedMethod('timeout', [Map, Closure], null)
        helper.registerAllowedMethod('timestamps', []) { println 'Printing timestamp' }
        helper.registerAllowedMethod('myMethod', [String, int]) { String s, int i ->
            println "Executing myMethod mock with args: '${s}', '${i}'"
        }
    }

    @Test
    public void configured() throws Exception {
        def script = loadScript('jobs/exampleJob.jenkins.groovy')
        script.call()
        printCallStack()
    }

}
