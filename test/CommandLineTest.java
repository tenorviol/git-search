import org.testng.Assert;
import org.testng.annotations.*;

import com.github.tenorviol.gitsearch.CommandLine;

public class CommandLineTest {

  @DataProvider(name = "invalid-command-lines")
  public Object[][] illegalCommandLineProvider() {
    return new Object[][] {
      { null },
      { "--index" },
      { "--index ./.index" },
      { "--index ./.index index" },
      { "--index ./.index query" },
      { "--index ./.index non-command" },
      { "query --index ./.index foo" },
    };
  }

  @Test(dataProvider       = "invalid-command-lines",
        expectedExceptions = IllegalArgumentException.class)
  public void testInvalidCommandLineThrowsIllegalArgumentException(String commandLine) {
    String[] args = (null == commandLine) ? new String[] {} : commandLine.split(" ");
    CommandLine cl = new CommandLine(args);
  }
}
