package earcut4j;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test suite that runs all JavaScript earcut test cases against the Java implementation
 */
public class JavaScriptTestSuite {
    
    private static JavaScriptTestData.TestExpectations expectations;
    private static Set<String> availableFixtures;
    
    @BeforeClass
    public static void setUp() throws IOException {
        expectations = JavaScriptTestData.loadExpectations();
        availableFixtures = JavaScriptTestData.getAvailableFixtures();
    }
    
    @Test
    public void testBasicIndices2D() {
        // Test case from JavaScript: indices-2d
        List<Integer> triangles = Earcut.earcut(new double[]{10, 0, 0, 50, 60, 60, 70, 10});
        Assert.assertArrayEquals(new Object[]{1, 0, 3, 3, 2, 1}, triangles.toArray());
    }

    @Test
    public void testBasicIndices3D() {
        // Test case from JavaScript: indices-3d
        List<Integer> triangles = Earcut.earcut(new double[]{10, 0, 0, 0, 50, 0, 60, 60, 0, 70, 10, 0}, null, 3);
        Assert.assertArrayEquals(new Object[]{1, 0, 3, 3, 2, 1}, triangles.toArray());
    }

    @Test
    public void testEmpty() {
        // Test case from JavaScript: empty
        List<Integer> triangles = Earcut.earcut(new double[]{});
        Assert.assertTrue(triangles.isEmpty());
    }

    /**
     * Dynamically run all available JavaScript test fixtures
     */
    @Test
    public void testAllJavaScriptFixtures() throws IOException {
        System.out.println("Running " + availableFixtures.size() + " JavaScript test fixtures...");
        
        int passed = 0;
        int skipped = 0;
        int failed = 0;
        
        for (String fixtureName : availableFixtures) {
            try {
                if (runFixtureTest(fixtureName)) {
                    passed++;
                } else {
                    skipped++;
                }
            } catch (Exception e) {
                System.err.println("Fixture " + fixtureName + " failed: " + e.getMessage());
                failed++;
            }
        }
        
        System.out.println("Test results: " + passed + " passed, " + skipped + " skipped, " + failed + " failed");
        
        // Fail the test if there were any failures
        if (failed > 0) {
            Assert.fail("Some fixtures failed: " + failed + " failures out of " + availableFixtures.size() + " total");
        }
    }
    
    /**
     * Runs a test for a specific fixture
     * @return true if test passed, false if skipped
     */
    private boolean runFixtureTest(String fixtureName) throws IOException {
        JavaScriptTestData.FlattenedData data = JavaScriptTestData.loadFixture(fixtureName);
        List<Integer> triangles = Earcut.earcut(data.vertices, data.holes, data.dimensions);
        
        // Get expected results
        int expectedTriangles = expectations.getExpectedTriangleCount(fixtureName);
        double expectedDeviation = expectations.getExpectedError(fixtureName);
        
        if (expectedTriangles > 0) {
            int actualTriangles = triangles.size() / 3;
            System.out.println("Testing fixture: " + fixtureName);
            System.out.println("Fixture " + fixtureName + ": expected " + expectedTriangles + " triangles, got " + actualTriangles);
            
            
            if (actualTriangles == expectedTriangles) {
                // Check deviation if expected
                if (expectedDeviation > 0) {
                    double deviation = Earcut.deviation(data.vertices, data.holes, data.dimensions, triangles);
                    System.out.println("Fixture " + fixtureName + ": deviation " + deviation + " (expected <= " + expectedDeviation + ")");
                    
                    if (deviation <= expectedDeviation) {
                        System.out.println("Fixture " + fixtureName + " passed!");
                        return true;
                    } else {
                        Assert.fail("Deviation too high for " + fixtureName + ": " + deviation + " > " + expectedDeviation);
                    }
                } else {
                    System.out.println("Fixture " + fixtureName + " passed!");
                    return true;
                }
            } else {
                Assert.fail("Triangle count mismatch for " + fixtureName + " expected:<" + expectedTriangles + "> but was:<" + actualTriangles + ">");
            }
        } else {
            // No expected result, just check that it doesn't crash
            System.out.println("Testing fixture: " + fixtureName + " (no expected result)");
            System.out.println("Fixture " + fixtureName + ": got " + (triangles.size() / 3) + " triangles");
            return true;
        }
        
        return false; // This should never be reached
    }
    
}