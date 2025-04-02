import org.junit.Assert;
import org.junit.Test;

import edu.nju.software.ast.LInteger;
import edu.nju.software.ast.LObject;
import edu.nju.software.ast.Parser;
import edu.nju.software.eval.Evaluator;
import edu.nju.software.stream.CharLookaheadStream;
import edu.nju.software.token.Lexer;

public class ControlTest {
  private void testFor(String input, LObject output) {
    Assert.assertEquals(output, Evaluator.eval(Parser.parse(Lexer.lex(new CharLookaheadStream(input)))));
  }

  @Test
  public void testDo() {
    testFor("(do (* 1 2) (- 1 2) (+ 1 2))", new LInteger(3));
  }

  @Test
  public void testIf() {
    testFor("(if 0 1 (+ 1 -1))", new LInteger(0));
  }

  @Test
  public void testFn() {
    testFor("((fn* (a) a) 123)", new LInteger(123));
  }

  @Test
  public void testComplex() {
    testFor("(do 2 3 ((fn* (a b c) (* (+ a b) c)) (if (do 0) 1 2) 3 4))", new LInteger(20));
  }

  @Test
  public void testEnv() {
    testFor("(let* ((f(fn* (b) b)) (b 0)) (f 0))", new LInteger(0));
  }
}