package io.indico.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestApiFailure.class,
    TestApiSuccess.class,
    TestVersioning.class
})
public class TestSuite {

}
