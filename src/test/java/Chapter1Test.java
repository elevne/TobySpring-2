import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.tobyspring.chapter1.Hi;
import org.tobyspring.chapter1.Person;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class Chapter1Test {

    // System.out.println 에 대한 단위테스트를 위해 설정해줌
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @Before
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void staticApplicationContextTest() {
        // StaticApplicationContext 는 코드에 의해 설정 메타정보를 등록하는 기능을 제공하는 애플리케이션 컨텍스트이다.
        StaticApplicationContext applicationContext = new StaticApplicationContext();
        // BeanDefinition 은 스프링의 설정 메타정보를 표현하는 추상 정보이다.
        BeanDefinition personDef = new RootBeanDefinition(Person.class);
        personDef.getPropertyValues().addPropertyValue("name", "Wonil");
        applicationContext.registerBeanDefinition("person", personDef);
        BeanDefinition hiDef = new RootBeanDefinition(Hi.class);
        hiDef.getPropertyValues().addPropertyValue("person", new RuntimeBeanReference("person"));
        applicationContext.registerBeanDefinition("hi", hiDef);

        Hi hi = applicationContext.getBean("hi", Hi.class);
        hi.print();
        assertEquals("'Hi, Wonil' was expected", "Hi, Wonil", outputStreamCaptor.toString().trim());
    }

    @Test
    public void genericApplicationContextTest() {
        // GenericApplicationContext 는 가장 일반적인 애플리케이션 컨텍스트의 구현체로, 실전에서 사용할 수 있는 모든 기능을 갖추고 있다.
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        // XmlBeanDefinitionReader 은 XML 로 작성된 빈 설정정보를 읽어서 컨테이너에 전달한다.
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
        reader.loadBeanDefinitions("/genericApplicationContext.xml");
        applicationContext.refresh();

        Hi hi = applicationContext.getBean("hi", Hi.class);
        hi.print();
        assertEquals("'Hi, elevne' was expected", "Hi, elevne", outputStreamCaptor.toString().trim());
    }

    @Test
    public void childParentContextTest() {
        // Parent context
        ApplicationContext parent = new GenericXmlApplicationContext("/parentContext.xml");
        // Child context
        GenericApplicationContext child = new GenericApplicationContext(parent);
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(child);
        reader.loadBeanDefinitions("/childContext.xml");
        child.refresh();

        Hi hi = child.getBean("hi2", Hi.class);
        hi.print();
        assertEquals("'Hi, Parent' was expected", "Hi, Parent", outputStreamCaptor.toString().trim());
    }

}
