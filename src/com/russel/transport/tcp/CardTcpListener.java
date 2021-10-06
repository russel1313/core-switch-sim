package com.russel.transport.tcp;

import com.russel.iso.AsciiXAPackager;
import com.russel.util.Alignment;
import com.russel.util.StringUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOField;
import org.jpos.iso.ISOMsg;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @author Rassul Hessampour
 * @version $Revision: 1.1.0 $
 */
public class CardTcpListener extends Thread {

    private DataOutputStream nextResponseISOMessageStream;
    private ServerSocket serverSocket;

    public CardTcpListener(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            while (!interrupted()) {
                synchronized (this) {
                    socket = serverSocket.accept();
                    String receivedMessage = readReceivedMessage(socket);
                    if(StringUtil.hasText(receivedMessage)) {
                        byte[] messageLength = new byte[4];
                        System.arraycopy(receivedMessage.getBytes("8859_1"), 0, messageLength, 0, messageLength.length);
                        if (isValidMessageLength(messageLength)) {
                            byte[] pureMessage = new byte[Integer.valueOf(new String(messageLength))];
                            System.arraycopy(receivedMessage.getBytes("8859_1"), messageLength.length, pureMessage, 0, pureMessage.length);
                            System.out.println("Next message length : " + new String(messageLength));
                            System.out.println("Next received message : " + new String(pureMessage) +
                                    " \nthrough port : " + serverSocket.getLocalPort() + " \nin time : " + new Date());
                            ISOMsg message = unpackReceivedMessageFromClient(pureMessage);
                            if(message != null) {
                                ISOMsg response = decideActionByMTI(message);
                                response = decideActionByProcessCode(message);
                                byte[] pureResponseMessage = response.pack();
                                String responseMessageLength =
                                        StringUtil.padZero(String.valueOf(pureResponseMessage.length), 4, Alignment.LEFT);
                                String finalResponseMessage = responseMessageLength + new String(pureResponseMessage, "8859_1");
                                nextResponseISOMessageStream = new DataOutputStream(socket.getOutputStream());
                                nextResponseISOMessageStream.write(finalResponseMessage.getBytes("8859_1"));
                                nextResponseISOMessageStream.flush();
                                if (!StringUtil.isEmpty(new String(messageLength))) {
                                    System.out.println("Next message length : " + new String(messageLength));
                                }
                                System.out.println("Next response message : " + new String(finalResponseMessage) +
                                        " \nin time : " + new Date());
                            }
                        }
                    }
                }
            }
            try {
                serverSocket.close();
                this.finalize();
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ISOException e) {
            e.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ISOMsg decideActionByMTI(ISOMsg message) throws Exception {
        if (message.getMTI().equals("1100")) {
            message.set(44, "05Test105Test2");
            message.setMTI("1110");
            message.set(39, "000");
            message.set(115, RandomStringUtils.randomNumeric(16));
        }
        else if (message.getMTI().equals("1420")) {
            message.setMTI("1430");
            message.set(39, "300");
        }
        else if (message.getMTI().equals("1804")) {
            message.setMTI("1814");
            message.set(39, "800");
        }
        else if (message.getMTI().equals("1200")) {
            message.setMTI("1210");
            message.set(39, "000");
        }
        else {
            message.setMTI("1210");
            message.set(39, "000");
        }
        message.recalcBitMap();
        return message;
    }

    private ISOMsg decideActionByProcessCode(ISOMsg message) throws Exception {
        if ("310000".equals(message.getString(3))) { //Balance Service
            // Add ledger and available balances
            message.set(54, "0001234500012345");
        }
        message.recalcBitMap();
        return message;
    }

    private boolean isValidMessageLength(byte[] messageLength) {
        return (!StringUtil.isEmpty(new String(messageLength)) &&
                !("    ".equals(new String(messageLength))) &&
                !("0000".equals(new String(messageLength))) &&
                (new String(messageLength).charAt(1) != '\0' &&
                        new String(messageLength).charAt(2) != '\0' &&
                        new String(messageLength).charAt(3) != '\0'));


    }

    private ISOMsg unpackReceivedMessageFromClient(byte[] receivedISOMessage) {
        if (receivedISOMessage != null && receivedISOMessage.length > 0) {
            AsciiXAPackager packager = new AsciiXAPackager();
            try {
                byte[] pureDate = new byte[receivedISOMessage.length];
                System.arraycopy(receivedISOMessage, 0, pureDate, 0, pureDate.length);
                ISOMsg message = new ISOMsg();
                message.setPackager(packager);
                message.unpack(pureDate);
                return message;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String readReceivedMessage(Socket socket) throws Exception {
        InputStream nextReceivedMessageStream = socket.getInputStream();
        if (null != nextReceivedMessageStream) {
            //read
            byte[] receivedMessage = new byte[500];
            nextReceivedMessageStream.read(receivedMessage);
            int counter = 0;
            for (int index = 0; index < receivedMessage.length; index++) {
                if (receivedMessage[index] != 0) {
                    counter++;
                }
                else {
                    break;
                }
            }
            byte[] finalByte = new byte[counter];
            for (int index = 0; index < counter; index++) {
                finalByte[index] = receivedMessage[index];
            }
            return new String(finalByte, "Cp1256");
        }
        return null;
    }
}