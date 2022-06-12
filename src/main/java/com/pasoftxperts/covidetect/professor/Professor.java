/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | Public record that holds a professor's email, name and id
 |
 |
*/

package com.pasoftxperts.covidetect.professor;

import java.io.Serializable;

public record Professor(String professorId, String professorName, String professorEmail) implements Serializable {}