package gn.web_app;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.JSONWriter.Feature;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;

public class BeanFactoryTests {

  @Configurable
  static class Config {
    @Bean
    public Bean1 bean1 () {
      return new Bean1();
    }

    @Bean
    public Bean2 bean2 () {
      return new Bean2();
    }
  }

  static class Bean1 {
    @Autowired
    private Bean2 bean2;

    public Bean2 getBean2() {
      return bean2;
    }
  }

  static class Bean2 {

  }

  @Test
  public void test() {
    final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

    final AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Config.class)
        .setScope(BeanDefinition.SCOPE_SINGLETON)
        .getBeanDefinition();

    beanFactory.registerBeanDefinition("config", beanDefinition);


    System.out.printf("bean定义名称: %s", Arrays.toString(beanFactory.getBeanDefinitionNames()));

    // Register all relevant annotation post processors in the given registry.
    // 给beanFactory添加"所有的相关注解"后置置处理器, 此时仅代表beanFactory中存在这些后置处理器, 具体创建
    // 时是否使用不归它管理.
    AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);

    System.out.printf("\nbean定义名称: %s", JSON.toJSONString(beanFactory.getBeanDefinitionNames(), Feature.PrettyFormat));

    // 让BeanFactoryPostProcessor开始工作
    beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values().forEach(
        beanPostProcessor -> beanPostProcessor.postProcessBeanFactory(beanFactory)
    );

    System.out.printf("\nbean定义名称: %s\n", JSON.toJSONString(beanFactory.getBeanDefinitionNames(), Feature.PrettyFormat));

    //System.out.printf("bean1:%s\n", beanFactory.getBean(Bean1.class));
    /*
     * todo 如果在未添加BeanPostProcessor时调用过getBean(Bean1), 此时bean1就完成了初始化, bean1中的bean2是null, 此后
    * 添加了后置处理器
     *
     */
    //System.out.printf("bean2 of bean1:%s\n", beanFactory.getBean(Bean1.class).getBean2());

    // Bean的后置处理器, 在创建bean时告诉beanFactory要执行哪些PostProcessor
    beanFactory.getBeansOfType(BeanPostProcessor.class).values().forEach(beanFactory::addBeanPostProcessor);
    System.out.printf("bean2 of bean1:%s\n", beanFactory.getBean(Bean1.class).getBean2());


  }
}
