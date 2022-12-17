package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**

 * @version 1.0
 * @description: TODO
 * @date 2022/11/27 15:29
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /** 
     * @Description:  员工登录
     * @param: request
     * @param: employee 
     * @return: com.itheima.reggie.common.R<com.itheima.reggie.entity.Employee>
     * @date: 2022/11/27 15:45
     */ 
    
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.将页面提交的密码password进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3.如果没有查询到就返回登录失败结果
        if (emp == null){
            return R.error("登陆失败");
        }

        //4.密码的比对，如果不一致则返回登陆失败的结果
        if (!emp.getPassword().equals(password)){
            return R.error("登陆失败");
        }

        //5.查看员工的状态，如果为禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("该账户已禁用");
        }

        //6.登陆成功，将员工id存入Session并返回登陆成功的结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /** 
     * @Description: 员工退出 
     * @param: request 
     * @return: com.itheima.reggie.common.R<java.lang.String>
     * @date: 2022/11/27 16:29
     */ 
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        // 清除Session中保存的当前员工登陆的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * @Description: 新增员工
     * @param: employee
     * @return: com.itheima.reggie.common.R<java.lang.String>
     * @date: 2022/11/28 18:43
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工信息：{}",employee.toString());
        //设置初始密码123456，需要进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        // employee.setCreateTime(LocalDateTime.now());
        // employee.setUpdateTime(LocalDateTime.now());
        //获得当前登录用户的id

        //  Long empId = (Long) request.getSession().getAttribute("employee");

        //  employee.setCreateUser(empId);
        //  employee.setUpdateUser(empId);

        employeeService.save(employee);
        return R.success("新增员工成功");
    }
   
   /** 
    * @Description: 员工信息的分页查询
    * @param: page
    * @param: pageSize
    * @param: name
    * @return: com.itheima.reggie.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
    * @date: 2022/11/28 19:53
    */
   @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
       log.info("page = {},pageSize = {},name = {}",page,pageSize,name);

       //构造分页构造器
       Page pageInfo = new Page(page,pageSize);

       //构造条件构造器
       LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();

       //添加过滤条件
       queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);

       //添加排序条件
       queryWrapper.orderByDesc(Employee::getUpdateTime);

       //执行查询
       employeeService.page(pageInfo,queryWrapper);
       return R.success(pageInfo);
    }
    /** 
     * @Description: 根据id修改员工信息 
     * @param: employee 
     * @return: com.itheima.reggie.common.R<java.lang.String>
     * @date: 2022/11/28 21:00
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody  Employee employee){
       log.info(employee.toString());
        //获得当前登录用户的id
      //  Long empId = (Long) request.getSession().getAttribute("employee");
      // employee.setUpdateTime(LocalDateTime.now());
      // employee.setUpdateUser(empId);
       employeeService.updateById(employee);
       return R.success("员工信息修改成功");
    }
    
    /** 
     * @Description: 根据id查询员工的信息
     * @param: id 
     * @return: com.itheima.reggie.common.R<com.itheima.reggie.entity.Employee>
     * @date: 2022/11/28 22:31
     */ 
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工的信息...");
        Employee employee = employeeService.getById(id);
        if (employee != null){
            return R.success(employee);
        }
       return R.error("没有查询到对应员工的信息");
    }
}
