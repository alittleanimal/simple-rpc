package consumer.service;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import provider.service.Calculator;
import request.CalculateRpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class CalculatorRemoteImpl implements Calculator {
    public static final int PORT = 9090;
    private static Logger log = LoggerFactory.getLogger(CalculatorRemoteImpl.class);

    public int add(int a, int b) {
        List<String> addressList = lookupProviders("Calculator.add");
        String address = chooseTarget(addressList);

        try {
            Socket socket = new Socket(address, PORT);

            // make request serialization
            CalculateRpcRequest calculateRpcRequest = generateRequest(a, b);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            // send request to server
            objectOutputStream.writeObject(calculateRpcRequest);

            // make response deserialization
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object response = objectInputStream.readObject();

            log.debug("response is {}", response);
            System.out.println("response is " + response.toString());


            if (response instanceof Integer) {
                return (Integer) response;
            } else {
                throw new InternalError();
            }

        } catch (UnknownHostException e) {
            log.error(" UnknownHostException fail", e);
            throw new InternalError();
        } catch (IOException e) {
            log.error("IOException fail", e);
            throw new InternalError();
        } catch (ClassNotFoundException e) {
            log.error(" ClassNotFoundException fail", e);
            throw new InternalError();
        }
    }

    private CalculateRpcRequest generateRequest(int a, int b) {
        CalculateRpcRequest calculateRpcRequest = new CalculateRpcRequest();
        calculateRpcRequest.setA(a);
        calculateRpcRequest.setB(b);
        calculateRpcRequest.setMethod("add");
        return calculateRpcRequest;
    }

    private String chooseTarget(List<String> providers) {
        if (null == providers || providers.size() == 0) {
            throw new IllegalArgumentException();
        }
        return providers.get(0);
    }

    public static List<String> lookupProviders(String s) {
        List<String> strings = new ArrayList<String>();
        strings.add("127.0.0.1");
        return strings;
    }
}
