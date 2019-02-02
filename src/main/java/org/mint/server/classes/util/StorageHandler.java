/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mint.server.classes.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;

public class StorageHandler {
  public static Response streamFile(String location, ServletContext context) {
    final File f = new File(location);
    if(!f.exists())
      return Response.status(Status.NOT_FOUND).build();
    if(!f.canRead()) 
      return Response.status(Status.FORBIDDEN).build();

    StreamingOutput stream = new StreamingOutput() {
      @Override
      public void write(OutputStream os) throws IOException {
        try {
          if(f.isDirectory())
            StorageHandler.streamDirectory(f, os);
          else
            StorageHandler.streamFile(f, os);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };

    String filename = f.getName();
    String mime = context.getMimeType(f.getAbsolutePath());
    if(f.isDirectory()) {
      filename += ".zip";
      mime = "application/zip";
    }

    return Response.ok(stream, mime)
        .header("content-disposition", "attachment; filename = "+ filename)
        .build();
  }

  private static void streamFile(File f, OutputStream os) {
    try {
      FileInputStream fin = new FileInputStream(f);
      IOUtils.copyLarge(fin, os);
      fin.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void streamDirectory(File directory, OutputStream os) {
    try {
      // Start the ZipStream reader. Whatever is read is streamed to response
      PipedInputStream pis = new PipedInputStream(2048);
      ZipStreamer pipestreamer = new ZipStreamer(pis, os);
      pipestreamer.start();

      // Start Zipping folder and piping to the ZipStream reader
      PipedOutputStream pos = new PipedOutputStream(pis);
      ZipOutputStream zos = new ZipOutputStream(pos);
      StorageHandler.zipAndStream(directory, zos, directory.getName() + "/");
      zos.flush();
      zos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void zipAndStream(File dir, ZipOutputStream zos, String prefix) 
      throws Exception {
    byte bytes[] = new byte[2048];
    for (File file : dir.listFiles()) {
      if(file.isDirectory())
        StorageHandler.zipAndStream(file, zos, prefix + file.getName() + "/" );
      else {
        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
        BufferedInputStream bis = new BufferedInputStream(fis);
        zos.putNextEntry(new ZipEntry(prefix + file.getName()));
        int bytesRead;
        while ((bytesRead = bis.read(bytes)) != -1) {
          zos.write(bytes, 0, bytesRead);
        }
        zos.closeEntry();
        bis.close();
        fis.close();
      }
    }
  }
}

class ZipStreamer extends Thread {
  public PipedInputStream pis;
  public OutputStream os;

  public ZipStreamer(PipedInputStream pis, OutputStream os) {
    super();
    this.pis = pis;
    this.os = os;
  }

  public void run() {
    try {
      IOUtils.copyLarge(pis, os);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}