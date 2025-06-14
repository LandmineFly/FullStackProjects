import com.leili.Main;
import com.leili.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Main.class)
public class TestJwt {
    @Test
    public void test1(){
        System.out.println(JwtUtils.generateToken("admin"));
    }
}
