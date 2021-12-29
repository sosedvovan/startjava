package com.urise.webapp.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import java.io.Serializable;

//его наследники тоже будут Serializable
@XmlAccessorType(XmlAccessType.FIELD)//анотац распр на наследников
public abstract class Section implements Serializable {
}
