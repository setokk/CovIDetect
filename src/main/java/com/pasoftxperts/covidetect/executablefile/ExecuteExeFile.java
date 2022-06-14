/*
 | Author: setokk
 | LinkedIn: https://www.linkedin.com/in/kostandin-kote-255382223/
 |
 |
 | Class Description:
 | This class is used to copy an .exe file from the resources folder
 | to the directory in which the .jar application was launched so that it can be executed
 |
*/

package com.pasoftxperts.covidetect.executablefile;

import com.pasoftxperts.covidetect.RunApplication;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExecuteExeFile
{
    public static OutputStream copyToTempDir(String name) throws IOException
    {
        // Gets program.exe from inside the jar file as an input stream.
        InputStream is = RunApplication.class.getResource("/com/pasoftxperts/covidetect/emailverifier/" + name).openStream();

        // Sets the output stream to a system folder.
        OutputStream os = new FileOutputStream(name);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1)
        {
            os.write(b, 0, length);
        }

        is.close();
        os.close();

        return os;
    }
}
