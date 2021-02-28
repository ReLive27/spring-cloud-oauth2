import com.netflix.discovery.converters.Auto;
import com.relive.ResourceServer;
import com.relive.service.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @Author ReLive
 * @Date 2021/2/27-15:47
 */
@SpringBootTest(classes = ResourceServer.class)
@RunWith(SpringRunner.class)
public class T {

    @Autowired
    private RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Test
    public void t(){
        System.out.println(passwordEncoder.encode("relive-client"));
    }
}
