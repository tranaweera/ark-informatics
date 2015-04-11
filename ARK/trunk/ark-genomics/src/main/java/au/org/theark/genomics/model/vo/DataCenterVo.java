package au.org.theark.genomics.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.spark.entity.MicroService;

public class DataCenterVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MicroService microService;
	
	private String name;
	
	private String directory;
	
	private String fileName;
	
	private List<DataSourceVo> dataSourceList;	

	public DataCenterVo() {
		this.dataSourceList = new ArrayList<DataSourceVo>();		
	}

	public MicroService getMicroService() {
		return microService;
	}

	public void setMicroService(MicroService microService) {
		this.microService = microService;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<DataSourceVo> getDataSourceList() {
		return dataSourceList;
	}

	public void setDataSourceList(List<DataSourceVo> dataSourceList) {
		this.dataSourceList = dataSourceList;
	}
	
}
