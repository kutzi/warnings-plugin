package hudson.plugins.warnings.parser;

import static junit.framework.Assert.*;
import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 * Tests the class {@link JavaDocParser}.
 */
public class JavaDocParserTest extends ParserTester {
    /** Error message. */
    private static final String WRONG_NUMBER_OF_WARNINGS_DETECTED = "Wrong number of warnings detected.";

    /**
     * Parses a file with two deprecation warnings.
     *
     * @throws IOException
     *      if the file could not be read
     */
    @Test
    public void parseJavaDocWarnings() throws IOException {
        Collection<FileAnnotation> warnings = new JavaDocParser().parse(openFile());

        assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 6, warnings.size());

        Iterator<FileAnnotation> iterator = warnings.iterator();
        FileAnnotation annotation = iterator.next();
        checkWarning(annotation,
                116,
                "Tag @link: can't find removeSpecChangeListener(ChangeListener, String) in chenomx.ccma.common.graph.module.GraphListenerRegistry",
                "/home/builder/hudson/workspace/Homer/oddjob/src/chenomx/ccma/common/graph/module/GraphListenerRegistry.java",
                JavaDocParser.WARNING_TYPE, StringUtils.EMPTY, Priority.NORMAL);
    }

    /**
     * Parses a warning log with 2 JavaDoc warnings.
     *
     * @throws IOException
     *      if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-4576">Issue 4576</a>
     */
    @Test
    public void issue4576() throws IOException {
        Collection<FileAnnotation> warnings = new JavaDocParser().parse(openFile("issue4576.txt"));

        assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 2, warnings.size());
        Iterator<FileAnnotation> iterator = warnings.iterator();
        checkWarning(iterator.next(),
                0,
                "Multiple sources of package comments found for package \"org.hamcrest\"",
                "-",
                JavaDocParser.WARNING_TYPE, StringUtils.EMPTY, Priority.NORMAL);
        checkWarning(iterator.next(),
                94,
                "@param argument \"<code>CoreAccountNumberTO</code>\" is not a parameter",
                "/home/hudson-farm/.hudson/jobs/farm-toplevel/workspace/farm-toplevel/service-module/src/main/java/com/rackspace/farm/service/service/CoreAccountServiceImpl.java",
                JavaDocParser.WARNING_TYPE, StringUtils.EMPTY, Priority.NORMAL);
    }

    /**
     * Parses a log with Junit message (false positive).
     *
     * @throws IOException
     *      if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8630">Issue 8630</a>
     */
    @Test
    public void issue8630() throws IOException {
        Collection<FileAnnotation> warnings = new JavaDocParser().parse(openFile("issue8630.txt"));

        assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 0, warnings.size());
    }

    /**
     * Parses a warning log with several JavaDoc warnings.
     *
     * @throws IOException
     *      if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-7718">Issue 7718</a>
     */
    @Test
    public void issue7718() throws IOException {
        Collection<FileAnnotation> warnings = new JavaDocParser().parse(openFile("issue7718.txt"));

        assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 7, warnings.size());
        Iterator<FileAnnotation> iterator = warnings.iterator();
        checkWarning(iterator.next(),
                0,
                "Text of tag @sys.prop in class ch.post.pf.mw.service.common.alarm.AlarmingService is too long!",
                "-",
                JavaDocParser.WARNING_TYPE, StringUtils.EMPTY, Priority.NORMAL);
        checkWarning(iterator.next(),
                57,
                "@(#) is an unknown tag.",
                "/u01/src/KinePolygon.java",
                JavaDocParser.WARNING_TYPE, StringUtils.EMPTY, Priority.NORMAL);
    }

    /** {@inheritDoc} */
    @Override
    protected String getWarningsFile() {
        return "javadoc.txt";
    }
}

