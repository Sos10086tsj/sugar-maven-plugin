package com.dreamferry.sugarmavenplugin;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * Description: Maven依赖版本更新
 * 
 * @author
 * @version 1.0.0 2020年10月9日 下午2:21:40
 */
@Mojo(name = "versionUpdate", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class VersionUpdate extends AbstractSugarMojo {

	/**
	 * 	项目根目录
	 */
	@Parameter(defaultValue = "${project.basedir}", property = "baseDir", required = true, readonly = true)
	private File baseDir;
	
	/**
	 * 	应用版本号节点名称
	 */
	@Parameter(defaultValue = "app.version", property = "sugar.versionKey", required = true, readonly = true)
	private String versionKey;
	
	/**
	 * 	应用版本号值
	 */
	@Parameter(defaultValue = "0.0.1", property = "sugar.versionValue", required = true, readonly = true)
	private String versionValue;
	
	/**
	 * 	是否开启动态版本号，默认不开启
	 */
	@Parameter(defaultValue = "false", property = "sugar.dynamicVersion", required = false, readonly = true)
	private Boolean dynamicVersion;
	
	/**
	 * 	版本类型，区分snapshot、release
	 */
	@Parameter(defaultValue = "SNAPSHOT", property = "sugar.versionType", required = false, readonly = true)
	private String versionType;

	@Override
	protected void executeProcess() throws MojoExecutionException, MojoFailureException {
		info("start updating version...");
		
		info("versionKey: " + versionKey);
		// 1.	读取pom文件，并修改
		for (File subFile : baseDir.listFiles()) {
			readPomAndUpdateVersion(subFile);
		}
		info("version updated");
	}

	private void readPomAndUpdateVersion(File file) {
		if (file.getName().equals("pom.xml")) {
			info("update pom " + file.getAbsolutePath());
			readAndUpdateTextValue(file, versionKey, versionValue);
		}else {
			if (file.isDirectory()) {
				if (file.getName().equals("target")) {// ignore target folder
					return;
				}
				for (File subFile : file.listFiles()) {
					readPomAndUpdateVersion(subFile);
				}
			}
		}
	}
	
	private void readAndUpdateTextValue(File file, String versionKey, String versionValue) {
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(file);
			Element root = doc.getRootElement();
			Element propElement = root.element("properties");
			for(Object o : propElement.elements()){
				Element ele = (Element)o;
				if (ele.getName().equalsIgnoreCase(versionKey)) {
					if (dynamicVersion) {// 如果需要动态后缀
						Date date = new Date();
						SimpleDateFormat format = new SimpleDateFormat("MMddHHmm");
						String suffix = format.format(date);
						
						versionValue = versionValue + "." + suffix;
					}
					versionValue = versionValue + "-" + versionType;
					info("update " + versionKey + " value from " + ele.getTextTrim() + " to " + versionValue);
					ele.setText(versionValue);
					break;
				}
			}
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(new FileWriter(file),format);
			writer.write(doc);
			writer.close();
		} catch (Exception e) {
			error(e);
		}
	}
}
