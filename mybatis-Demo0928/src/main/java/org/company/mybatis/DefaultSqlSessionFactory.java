package org.company.mybatis;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

//作用1:解析文本配置文件(mapper.xml和jdbc.properties文件),并复制到configuration对象
//作用2:生成sqlSession对象,并把configuration对象 放入其中
public class DefaultSqlSessionFactory {
	private Configurations configurations = new Configurations();

	public DefaultSqlSessionFactory() {
		loadDbInfo();		// 1.读properties文件,并放入configuration中
		loadMappersInfo();	// 2.读mapperxml文件,并放入configuration中
	}

	// openSession方法返回一个Session对象并把从xml文件中解析好的configurations放在其中
	public DefaultSqlSession openSession() {
		return new DefaultSqlSession(configurations);
	}

	// 记录文件的位置
	private static final String jdbcPropertiesFile = "jdbc.properties";//路径问题
	private static final String MappersLocation = "mappers";

	// 把链接的配置信息放入configurations中
	private void loadDbInfo() {
		//通过类加载器加载jdbc.properties到内存中
		InputStream rs = DefaultSqlSessionFactory.class.getClassLoader().getResourceAsStream(jdbcPropertiesFile);
		Properties properties = new Properties();
		try {
			properties.load(rs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//把jdbc.properties的信息放入configurations对象中
		configurations.setJdbcDriver(properties.get("jdbc.driver").toString());
		configurations.setJdbcUrl(properties.get("jdbc.url").toString());
		configurations.setUsername(properties.get("jdbc.username").toString());
		configurations.setPassword(properties.get("jdbc.password").toString());
	}

	// 遍历mapper文件夹下所有xml文件
	private void loadMappersInfo() {
		//classloader的功能是把class文件的相关信息从磁盘中加载到内存里,getResource方法就是扫描class文件夹下的资源
		URL resource = DefaultSqlSessionFactory.class.getClassLoader().getResource(MappersLocation);// URL:Uniform Resource Locator
		File file = new File(resource.getFile());
		if (file.isDirectory()) {
			File[] listFiles = file.listFiles();
			for (File file2 : listFiles) {
				loadSqlsInfoInMappers(file2);
			}
		}
	}

	// 遍历xml文件中所有sql语句对象的信息
	private void loadSqlsInfoInMappers(File file) {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(file); //先生成文件对象
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element rootElement = document.getRootElement();//由文件对象找到根节点
		List<Element> elements = rootElement.elements("select");	//在跟节点中找到所有的select标签
		// 把所有的select标签的所有信息拿出来放入MapperStatment对象中保存,并传给configurations对象
		for (Element element : elements) {
			MapperStatment mapperStatment = new MapperStatment();
			//拿出信息
			String namespace = rootElement.attribute("namespace").getValue();
			String id = element.attribute("id").getValue();
			String sourceId = namespace + "." + id;
			//放入mapperStatment对象中
			mapperStatment.setNamespace(namespace);
			mapperStatment.setId(id);
			mapperStatment.setResultType(element.attribute("resultType").getValue());
			mapperStatment.setSql(element.getData().toString());
			//放入configurations中(也就是放入其中的map里)
			configurations.getMap().put(sourceId, mapperStatment);
//			System.out.println(configurations);
//			System.out.println(configurations.getMap().get(sourceId).toString());
		}
	}

}
