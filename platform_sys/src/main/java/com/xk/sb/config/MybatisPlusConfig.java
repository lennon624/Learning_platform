package com.xk.sb.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus配置
 * @author bill
 *
 * MapperScan("com.demo.mapper")：扫描指定包中的接口
 *
 * MapperScan("com.demo.*.mapper")：一个*代表任意字符串，但只代表一级包,比如可以扫到com.demo.aaa.mapper,不能扫到com.demo.aaa.bbb.mapper
 *
 * MapperScan("com.demo.**.mapper")：两个*代表任意个包,比如可以扫到com.demo.aaa.mapper,也可以扫到com.demo.aaa.bbb.mapper
 */

@Configuration
@MapperScan("com.xk.sb.**.mapper")
public class MybatisPlusConfig {
	
	/**
     * mybatis-plus分页插件<br>
     *
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }

}
