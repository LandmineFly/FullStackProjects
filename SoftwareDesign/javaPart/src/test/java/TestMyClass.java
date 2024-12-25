import com.leili.Main;
import com.leili.mapper.UserMapper;
import com.leili.pojo.User;
import com.leili.pojo.UserSelectOption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;

@SpringBootTest(classes = Main.class)
public class TestMyClass {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test1() {
        System.out.println(userMapper.login(new User("12","12")));
    }
    @Test
    public void test2() {
        User user = new User("122","12",0);
        if (userMapper.isDuplicate(user) == 1) {
            System.out.println("用户名重复，注册失败");
            return;
        }
        int insert = userMapper.insert(user);
        System.out.println("成功");
    }

    @Test
    public void test3() {
        User oldUser = new User("11111",null,null);
        User newUser = new User("2222","2222",0);
        HashMap<String, User> users = new HashMap<>();
        users.put("old",oldUser);
        users.put("new",newUser);
        if (userMapper.isDuplicate(users.get("old")) == 0){
            System.out.println("用户名不存在，修改失败");
        }
        if (userMapper.isDuplicate(users.get("new")) == 1){
            System.out.println("用户名重复，修改失败");
        }
        int update = userMapper.updateByUsr(users);
        System.out.println("ok");
    }

    @Test
    public void test4() {
        User user = new User("asdf",null,null);
        if (userMapper.isDuplicate(user) == 0){
            System.out.println("用户名不存在，删除失败");
        }
        int update = userMapper.deleteByUsr(user);
        System.out.println("ok");
    }

    @Test
    public void test5() {
        UserSelectOption userSelectOption = new UserSelectOption();
        userSelectOption.setPage(1);
        userSelectOption.setItemsPerPage(10);
        userSelectOption.setSortBy(null);
        userSelectOption.setSearchBy(new User(null,null,1));
        userSelectOption.setOffset(userSelectOption.getItemsPerPage() * (userSelectOption.getPage() - 1));
        List<User> users = userMapper.select(userSelectOption);
        System.out.println(users);
    }

}
