package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 * test methods can be of any name, better to follow the convention
 * test methods are identified as test methods by JUnit when you put the @Test
 */
public class FlooringMasteryMainAppTest
    extends TestCase
{
    /**
     *
     * Can write a
     * @ BeforeAll
     * @ BeforeEach
     * @ AfterAll
     * @ AfterEach
     *
     * Create the test case
     *
     * @param testName name of the test case
     *
     */
    public FlooringMasteryMainAppTest(String testName )
    {
        super( testName );
    }

    /**
     * write the expected output
     *  write the expected output
     *
     *  write assertions -  example:- Assertions.assertEquals(expectedOutput, actualOutput)
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( FlooringMasteryMainAppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}
