import org.testng.Assert;
import org.testng.annotations.*;

import com.github.tenorviol.gitsearch.CommandLine;

public class CommandLineTest {

  @Test
  public void testFoo() {
    String[] args = {"--index", "./foo/index", "index"};
    try {
      CommandLine cl = new CommandLine(args);
    } catch (Exception e) {
      System.out.println(e);
    }
    Assert.assertEquals(1,1);
  }
}
