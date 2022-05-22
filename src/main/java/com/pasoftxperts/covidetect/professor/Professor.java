package com.pasoftxperts.covidetect.professor;

import java.io.Serializable;

public record Professor(String professorId, String professorName, String professorEmail) implements Serializable {}