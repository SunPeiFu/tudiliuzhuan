package com.tudiliuzhuan;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * mysql 代码生成器
 *
 * @author sunpeifu
 * @since 2020-02-06
 */
public class MysqlCodeGenerator {

    // 需要关注修改的
    /**
     * 作者
     */
    private static String author = "mybatis-plus";
    /**
     * 需要去掉的表前缀
     */
    public static String[] TABLE_PREFIX = {"claim_c_"};

    /**
     * 需要去掉的字段前缀
     */
    public static String[] FILED_PREFIX = {"is_"};
    /**
     * 需要生成的表名(两者只能取其一)
     */
    public static String[] INCLUDE_TABLES = {"claim_c_apply_claim_third_mapping"};
    /**
     * 需要排除的表名(两者只能取其一)
     */
    public static String[] EXCLUDE_TABLES = {};
    /**
     * 代码生成的包名
     */
    public static String PACKAGE_NAME = "com.tudiliuzhuan";

    // 下面一般不需要动
    /**
     * 代码生成的模块名
     */
    public static String CODE_NAME = "exapi";

    /**
     * 前端代码生成地址--vue
     */
    //public static String PACKAGE_WEB_DIR = "D://temp//esbubcp//autocode//vue//";

    /**
     * 是否包含基础业务字段
     */
    public static Boolean HAS_SUPER_ENTITY = Boolean.FALSE;
    /**
     * 基础业务字段
     */
    public static String[] SUPER_ENTITY_COLUMNS = {"id", "create_time", "create_user", "update_time", "update_user", "status", "is_deleted"};

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        // 直接生成在工程目录中
        String projectPath = "/Users/daike/Desktop/workspace/TuDiLiuZhuan";
        // String projectPath = System.getProperty("user.dir") + "/Users/daike/Desktop/workspace/TuDiLiuZhuan/TuDiLiuZhuan";
        // 去掉接口前面的I
        gc.setServiceName("%sService");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(author);
        gc.setFileOverride(true);
        gc.setOpen(false);
        //通用查询映射结果
        gc.setBaseResultMap(true);
        //通用查询结果列
        gc.setBaseColumnList(true);
        //是否生成swagger2注解
        gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://172.16.0.227:3306/v3_cloud_ins");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("cloud_ins");
        dsc.setPassword("875e-d928dbfAc");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(null);
        pc.setParent(PACKAGE_NAME);
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称，输出至resources目录
                return projectPath + "/src/main/resources/mapper/"
                        + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.entityTableFieldAnnotationEnable(true);
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);

        if (HAS_SUPER_ENTITY) {
            strategy.setSuperControllerClass("com.ebtech.esbubcp.common.system.base.controller.BaseController");
            strategy.setSuperEntityClass("com.ebtech.esbubcp.common.system.base.entity.BaseEntity");
            strategy.setSuperEntityColumns(SUPER_ENTITY_COLUMNS);
            strategy.setSuperServiceClass("com.ebtech.esbubcp.common.system.base.service.BaseService");
            strategy.setSuperServiceImplClass("com.ebtech.esbubcp.common.system.base.service.impl.BaseServiceImpl");
        } else {
            strategy.setSuperServiceClass("com.baomidou.mybatisplus.extension.service.IService");
            strategy.setSuperServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl");
        }

        if (INCLUDE_TABLES.length > 0) {
            strategy.setInclude(INCLUDE_TABLES);
        }
        if (EXCLUDE_TABLES.length > 0) {
            strategy.setExclude(EXCLUDE_TABLES);
        }
        ////strategy.setSuperEntityColumns("id");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(TABLE_PREFIX);
        strategy.setFieldPrefix(FILED_PREFIX);
        mpg.setStrategy(strategy);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
