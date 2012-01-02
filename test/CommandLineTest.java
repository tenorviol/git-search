import org.testng.Assert;
import org.testng.annotations.*;

import com.github.tenorviol.gitsearch.CommandLine;

import java.util.Arrays;
import java.util.List;

public class CommandLineTest {

  @DataProvider(name = "invalid-command-lines")
  public Object[][] illegalCommandLineProvider() {
    return new Object[][] {
      { null },
      { "--index" },
      { "--index path/to/index" },
      { "--index path/to/index index" },
      { "--index path/to/index query" },
      { "--index path/to/index non-command" },
      { "query --index path/to/index add/to/index" },
    };
  }

  @Test(dataProvider       = "invalid-command-lines",
        expectedExceptions = IllegalArgumentException.class)
  public void testInvalidCommandLineThrowsIllegalArgumentException(String commandLine) {
    String[] args = (null == commandLine) ? new String[] {} : commandLine.split(" ");
    CommandLine cl = new CommandLine(args);
  }
  
  @DataProvider(name = "command-lines")
  public Object[][] commandLineProvider() {
    return new Object[][] {
      { "--index path/to/index index add/to/index second/add/to/index" },
      { "--index path/to/index query word1 word2 field:word3" },
    };
  }
  
  @Test(dataProvider = "command-lines")
  public void testIndexCommand(String commandLine) {
    String[] args = commandLine.split(" ");
    CommandLine cl = new CommandLine(args);
    
    Assert.assertEquals(cl.indexPath,          args[1]);
    Assert.assertEquals(cl.command,            args[2]);
    Assert.assertEquals(cl.commandArgs.size(), args.length - 3);
    
    List list = Arrays.asList(args);
    list = list.subList(3, list.size());
    Assert.assertEquals(cl.commandArgs, list);
  }
}
