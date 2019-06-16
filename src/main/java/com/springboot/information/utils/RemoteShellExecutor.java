package com.springboot.information.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class RemoteShellExecutor {

    private Connection conn;
    /** 远程机器IP */
    private String ip;
    /** 用户名 */
    private String osUsername;
    /** 密码 */
    private String password;
    private String charset = Charset.defaultCharset().toString();

    private static final int TIME_OUT = 1000 * 5 * 60;

    /**
     * 构造函数
     * @param ip
     * @param usr
     * @param pasword
     */
    public RemoteShellExecutor(String ip, String usr, String pasword) {
        this.ip = ip;
        this.osUsername = usr;
        this.password = pasword;
    }


    /**
     * 登录
     * @return
     * @throws IOException
     */
    private boolean login() throws IOException {
        conn = new Connection(ip);
        conn.connect();
        return conn.authenticateWithPassword(osUsername, password);
    }

    /**
     * 执行脚本
     *
     * @param cmds
     * @return
     * @throws Exception
     */
    public int exec(String cmds) throws Exception {
        InputStream stdOut = null;
        InputStream stdErr = null;
        String outStr = "";
        String outErr = "";
        int ret = -1;
        try {
            if (login()) {
                System.out.println("login success");
                // Open a new {@link Session} on this connection
                Session session = conn.openSession();
                // Execute a command on the remote machine.
                System.out.println("session success");
                session.execCommand(cmds);
                System.out.println("exec:"+cmds);
                System.out.println("session:"+session);
                stdOut = new StreamGobbler(session.getStdout());
                System.out.println("stdOut:"+stdOut.toString());
                outStr = processStream(stdOut, charset);
                System.out.println("stdOut");


                stdErr = new StreamGobbler(session.getStderr());
                outErr = processStream(stdErr, charset);
                System.out.println("stdErr");

                session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);

                System.out.println("outStr=" + outStr);
                System.out.println("outErr=" + outErr);

                ret = session.getExitStatus();
            } else {
                throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return ret;
    }

    /**
     * @param in
     * @param charset
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    private String processStream(InputStream in, String charset) throws Exception {
        System.out.println("ininin");
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        int num =0;
        while (in.read(buf) != -1) {
            num++;
            System.out.println("number:"+num);
            sb.append(new String(buf, charset));
            System.out.println("sb:"+sb.toString());
            System.out.println("buf:"+in.read(buf));

        }
        System.out.println("enene22");
        return sb.toString();
    }

    public static void main(String args[]) throws Exception {
        RemoteShellExecutor executor = new RemoteShellExecutor("59.78.194.14", "zh", "b1006");
        // 执行myTest.sh 参数为java Know dummy
        System.out.println(executor.exec("/home/zh/Documents/start/note.sh"));
    }
}