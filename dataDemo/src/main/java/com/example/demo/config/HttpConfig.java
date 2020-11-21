package com.example.demo.config;

import com.example.demo.filter.LoginFilter;
import com.example.demo.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class HttpConfig extends WebSecurityConfigurerAdapter
{


    @Autowired
    StudentService studentService;

    @Bean
    public UserDetailsService myUserService(){
        return new StudentService();
    }

    @Bean
    //依赖注入
    PasswordEncoder passwordEncoder() {
        //因为SpringSecurity不推荐无密码保护 但是这里只是测试之用
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 配置登录的过滤器
     * @return
     * @throws Exception
     */
    @Bean
    LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setAuthenticationManager(super.authenticationManagerBean());
        loginFilter.setFilterProcessesUrl("/login");
        loginFilter.setAuthenticationSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
                //1.获取认证资料
                Object principle = authentication.getPrincipal();

                //2.设置会写文件格式
                httpServletResponse.setContentType("application/json; charset=utf-8");

                //3.获取输出流
                PrintWriter printWriter = httpServletResponse.getWriter();

                /**
                 * 4.开始回复
                 */
                //第一步 返回状态码200
                httpServletResponse.setStatus(200);
                //第二步 装填json对象 可以先装填一个HashMap 再把它转成json
                Map<String, Object> map = new HashMap<String, Object>();

                map.put("status", 200); //搞不懂为什么还要再写一次状态码
                map.put("msg", principle); //用户信息写在消息体里

                //这是用于快速把一个map转成json格式的String的工具
                ObjectMapper om = new ObjectMapper();
                //这样就可以把map转化为可以快捷传输的jsonString了
                String respJson = om.writeValueAsString(map);

                //这里用println应该也没问题吧
                printWriter.write(respJson);
                //清空缓存然后关闭 真有点像写课设的时候用socket啊
                printWriter.flush();
                printWriter.close();
            }
        );
        loginFilter.setAuthenticationFailureHandler((httpServletRequest, httpServletResponse, e) -> {
                PrintWriter out = httpServletResponse.getWriter(); //1.
                /**
                 * 代表未获得认证的状态码是401
                 */
                httpServletResponse.setStatus(401); //2.



                //一定注意设置返回值类型
                httpServletResponse.setContentType("application/json; charset=utf-8");

                String failReason = "";

                if(e instanceof BadCredentialsException)
                    failReason = "bad credentials";
                else
                    failReason = "unknown error";


                //装填数据 装两项 状态和信息
                Map<String, Object> map = new HashMap<>();
                map.put("status", 401);
                map.put("msg", failReason);

                //转成json并发布
                ObjectMapper om = new ObjectMapper();
                out.write(om.writeValueAsString(map));

                //记得关掉数据流!
                out.flush();
                out.close();
                }
        );

        ConcurrentSessionControlAuthenticationStrategy sessionStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        sessionStrategy.setMaximumSessions(1);
        loginFilter.setSessionAuthenticationStrategy(sessionStrategy);
        return loginFilter;
    }

    @Bean
    SessionRegistryImpl sessionRegistry() {
        return new SessionRegistryImpl();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //数据库中认证
        auth.userDetailsService(myUserService());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors();

        //配置需要被保护的页面以及相应的权限 和 登录页面的路由 登录方式(此处为表单形式)  以及对什么样的请求需要认证
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginProcessingUrl("/doLogin")
                .usernameParameter("name")
                .passwordParameter("password")
                /**
                 * 这个处理方法是负责认证成功后写回请求的
                 * 具体使用方法是传入一个刚构成的AuthenticationSuccessHandler 重写其onAuc..Suc..方法
                 * 这个方法要传入三个参数 一为HttpServletRequest对象res 二为 Http...Response rep对象
                 * 三就是这个认证本身的对象 Authentication auth 负责提供此认证用户的信息 一道写回去
                 * 这三个方法默认都可由SpringSecurity获取并提供
                 *
                 * 如果登录请求成功了的话会自动转接到以下这个处理函数中来
                 */
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {



                        //1.获取认证资料
                        Object principle = authentication.getPrincipal();

                        //2.设置会写文件格式
                        httpServletResponse.setContentType("application/json; charset=utf-8");

                        //3.获取输出流
                        PrintWriter printWriter = httpServletResponse.getWriter();

                        /**
                         * 4.开始回复
                         */
                        //第一步 返回状态码200
                        httpServletResponse.setStatus(200);
                        //第二步 装填json对象 可以先装填一个HashMap 再把它转成json
                        Map<String, Object> map = new HashMap<String, Object>();

                        map.put("status", 200); //搞不懂为什么还要再写一次状态码
                        map.put("msg", principle); //用户信息写在消息体里

                        //这是用于快速把一个map转成json格式的String的工具
                        ObjectMapper om = new ObjectMapper();
                        //这样就可以把map转化为可以快捷传输的jsonString了
                        String respJson = om.writeValueAsString(map);

                        //这里用println应该也没问题吧
                        printWriter.write(respJson);
                        //清空缓存然后关闭 真有点像写课设的时候用socket啊
                        printWriter.flush();
                        printWriter.close();
                    }
                })
                /**
                 * 登录失败时的处理函数 参数也是给被重写了 on..Fail..方法的处理器对象
                 * 这个被重写的方法大致上和上面那个on..Suc..差不多 但是它的信息(第3个)参数变为了
                 * Auth..Exception e 代表的是认证失败的原因 可以依此对前台作必要的回复
                 * 其它步骤则差不多 都是0.获取成功/失败信息 1.获取输出流 2.response设置状态码 3.装填数据并回复
                 */
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        PrintWriter out = httpServletResponse.getWriter(); //1.
                        /**
                         * 代表未获得认证的状态码是401
                         */
                        httpServletResponse.setStatus(401); //2.

                        System.out.println(httpServletRequest.getParameter("name"));
                        System.out.println(httpServletRequest.getParameter("password"));


                        //一定注意设置返回值类型
                        httpServletResponse.setContentType("application/json; charset=utf-8");

                        String failReason = "";

                        if(e instanceof BadCredentialsException)
                            failReason = "bad credentials";
                        else
                            failReason = "unknown error";


                        //装填数据 装两项 状态和信息
                        Map<String, Object> map = new HashMap<>();
                        map.put("status", 401);
                        map.put("msg", failReason);

                        //转成json并发布
                        ObjectMapper om = new ObjectMapper();
                        out.write(om.writeValueAsString(map));

                        //记得关掉数据流!
                        out.flush();
                        out.close();
                    }
                })
                .permitAll().and()
                .csrf().disable();

                /**
                 * 这个login_page如果没有指定 那么就会跳默认可视化页面(可见这是可视化界面的路由)
                 * 如果指定了这个路由 但是找不到对应的page(前台没写) 那么就会报错
                 */
                //暂时不需要把前台页面放到后台来 开发环境中只需要后台的接口 --11/10
//                .loginPage("/index.html") //这应该是前台的一个路由罢 登录界面就是它了
//                .loginProcessingUrl("/login") //负责处理登录的路由 话说和上面那个有区别吗
//                .usernameParameter("name")   //这两段应该是post请求里的参数
//                .passwordParameter("password")


        http.addFilterAt(new ConcurrentSessionFilter(sessionRegistry(), event -> {
            HttpServletResponse resp = event.getResponse();
            resp.setContentType("application/json;charset=utf-8");
            resp.setStatus(401);
            PrintWriter out = resp.getWriter();

            //装填数据 装两项 状态和信息
            Map<String, Object> map = new HashMap<>();
            map.put("status", 401);
            map.put("msg", "您已在另一台设备登录，本次登录已下线!");


            out.write(new ObjectMapper().writeValueAsString(map));
            out.flush();
            out.close();
        }), ConcurrentSessionFilter.class);

    }
}
