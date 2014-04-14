package cn.damai.boss.projectreport.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Upt01SearchTemplate entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "upt01_search_template")
public class Upt01SearchTemplate implements java.io.Serializable {

	// Fields

	private Long templateId;         //模板id
	private Long userId;             //当前登录maitix用户
	private String templateName;     //模板名称
	private String templateContent;  //模板内容
	private Date createTime;    //创建时间
	private Date modifyTime;    //修改时间
	private Short status;            //状态  1：正常；2：删除

	// Constructors

	/** default constructor */
	public Upt01SearchTemplate() {
	}

	/** minimal constructor */
	public Upt01SearchTemplate(Long userId, String templateName, Timestamp createTime, Timestamp modifyTime, Short status) {
		this.userId = userId;
		this.templateName = templateName;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.status = status;
	}

	/** full constructor */
	public Upt01SearchTemplate(Long userId, String templateName, String templateContent, Timestamp createTime,
			Timestamp modifyTime, Short status) {
		this.userId = userId;
		this.templateName = templateName;
		this.templateContent = templateContent;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.status = status;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "template_id", unique = true, nullable = false)
	public Long getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	@Column(name = "user_id", nullable = false)
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "template_name", nullable = false, length = 50)
	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@Column(name = "template_content", length = 65535)
	public String getTemplateContent() {
		return this.templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "modify_time", nullable = false, length = 19)
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "status", nullable = false)
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

}