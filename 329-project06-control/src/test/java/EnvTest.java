import edu.nju.software.ast.LInteger;
import edu.nju.software.ast.LList;
import edu.nju.software.ast.LObject;
import edu.nju.software.ast.Parser;
import edu.nju.software.eval.Evaluator;
import edu.nju.software.stream.CharLookaheadStream;
import edu.nju.software.token.Lexer;
import org.junit.Assert;
import org.junit.Test;

public class EnvTest {
    private void testFor(String input, LObject output) {
        Assert.assertEquals(output, Evaluator.eval(Parser.parse(Lexer.lex(new CharLookaheadStream(input)))));
    }

    @Test
    public void testLet() {
        testFor("(let ((x 1)) x)", new LInteger(1));
    }

    @Test
    public void testCalculated() {
        testFor("(let ((x (+ 1 2))) x)", new LInteger(3));
    }

    @Test
    public void testLetStar() {
        testFor("(let* ((x (+ 1 2)) (y (* 2 x))) y)", new LInteger(6));
    }

    @Test
    public void testCalculation() {
        testFor("(let* ((x (+ 1 2)) (y (* 2 x))) (- y x))", new LInteger(3));
    }

    @Test
    public void testNested() {
        testFor("(let* ((x (+ 1 2)) (y (* 2 x))) (let ((x 10) (z (* x y))) z))", new LInteger(18));
    }

    @Test
    public void testList() {
        testFor("(let* ((x (+ 1 2)) (y (* 2 x))) (let ((z (cons y (cons x nil)))) z))",
                new LList(new LInteger(6), new LInteger(3)));
    }
}