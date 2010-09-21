package au.org.theark.study.model.vo;

import java.io.Serializable;
import java.util.*;

import au.org.theark.study.model.entity.StudyComp;
/**
 * A container for Study Component related function. 
 * @author nivedann
 *
 */
public class StudyCompVo implements Serializable{
	
	private StudyComp studyComponent;
	private List<StudyComp> studyCompList;
	private int mode;
	
	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public List<StudyComp> getStudyCompList() {
		return studyCompList;
	}

	public void setStudyCompList(List<StudyComp> studyCompList) {
		this.studyCompList = studyCompList;
	}

	public StudyComp getStudyComponent() {
		return studyComponent;
	}

	public void setStudyComponent(StudyComp studyComponent) {
		this.studyComponent = studyComponent;
	}

}
