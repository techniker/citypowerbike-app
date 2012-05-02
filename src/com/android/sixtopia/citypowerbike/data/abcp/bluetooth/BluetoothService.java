package com.android.sixtopia.citypowerbike.data.abcp.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.android.sixtopia.citypowerbike.util.Tags;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

public class BluetoothService {
	
	 // Debugging
    private static final boolean D = true;
	
	private final BluetoothAdapter btAdapter;
	private final Handler handler;
	private ConnectThread connectThread;
	private ConnectedThread connectedThread;
	
	private ReceiveListener receiveListener;
	
	private int state;
	
	 // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    
    public static boolean STARTBYTE_RECEIVED = false;
    public static boolean LENGTH_RECEIVED = false;
	
    public BluetoothService(Handler handler, ReceiveListener receiveListener){
    	this.btAdapter = BluetoothAdapter.getDefaultAdapter();
    	this.handler = handler;
    	this.receiveListener = receiveListener;
    	this.state = this.STATE_NONE;
    }
    
    /**
     * Set the current state of the chat connection
     * @param state  An integer defining the current connection state
     */
    private synchronized void setState(int state) {
        if (D) Log.d(Tags.LOG_TAG, "setState() " + this.state + " -> " + state);
        this.state = state;
        //TODO:
        // Give the new state to the Handler so the UI Activity can update
//        mHandler.obtainMessage(BlueTerm.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }
    
    /**
     * Return the current connection state. */
    public synchronized int getState() {
        return state;
    }
    
    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume() */
    public synchronized void start() {
        if (D) Log.d(Tags.LOG_TAG, "start");

        // Cancel any thread attempting to make a connection
        if (connectThread != null) {
        	connectThread.cancel(); 
        	connectThread = null;
        }

        // Cancel any thread currently running a connection
        if (connectedThread != null) {
        	connectedThread.cancel(); 
        	connectedThread = null;
        }

        setState(STATE_NONE);
    }
    
    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device) {
        
        device = btAdapter.getRemoteDevice("00:12:6F:07:59:7B");
        if (D) Log.d(Tags.LOG_TAG, "connect to: " + device);
        // Cancel any thread attempting to make a connection
        if (state == STATE_CONNECTING) {
            if (connectThread != null) {connectThread.cancel(); connectThread = null;}
        }

        // Cancel any thread currently running a connection
        if (connectedThread != null) {connectedThread.cancel(); connectedThread = null;}

        // Start the thread to connect with the given device
        connectThread = new ConnectThread(device);
        connectThread.start();
        setState(STATE_CONNECTING);
    }
    
    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        if (D) Log.d(Tags.LOG_TAG, "connected");

        // Cancel the thread that completed the connection
        if (connectThread != null) {
        	connectThread.cancel(); 
        	connectThread = null;
        }

        // Cancel any thread currently running a connection
        if (connectedThread != null) {
        	connectedThread.cancel(); 
        	connectedThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        connectedThread = new ConnectedThread(socket, receiveListener);
        connectedThread.start();

        //TODO:
        // Send the name of the connected device back to the UI Activity
//        Message msg = mHandler.obtainMessage(BlueTerm.MESSAGE_DEVICE_NAME);
//        Bundle bundle = new Bundle();
//        bundle.putString(BlueTerm.DEVICE_NAME, device.getName());
//        msg.setData(bundle);
//        mHandler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }
    
    /**
     * Stop all threads
     */
    public synchronized void stop() {
        if (D) Log.d(Tags.LOG_TAG, "stop");


        if (connectThread != null) {
        	connectThread.cancel(); 
        	connectThread = null;
        }

        if (connectedThread != null) {
        	connectedThread.cancel(); 
        	connectedThread = null;
        }

        setState(STATE_NONE);
    }
    
    /**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread tmpConnectedThread;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (state != STATE_CONNECTED) return;
            tmpConnectedThread = connectedThread;
        }
        // Perform the write unsynchronized
        tmpConnectedThread.write(out);
    }
    
    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        setState(STATE_NONE);

        //TODO:
        // Send a failure message back to the Activity
//        Message msg = mHandler.obtainMessage(BlueTerm.MESSAGE_TOAST);
//        Bundle bundle = new Bundle();
//        bundle.putString(BlueTerm.TOAST, "Unable to connect device");
//        msg.setData(bundle);
//        mHandler.sendMessage(msg);
    }
    
    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        setState(STATE_NONE);
        
        //TODO:
        // Send a failure message back to the Activity
//        Message msg = mHandler.obtainMessage(BlueTerm.MESSAGE_TOAST);
//        Bundle bundle = new Bundle();
//        bundle.putString(BlueTerm.TOAST, "Device connection was lost");
//        msg.setData(bundle);
//        mHandler.sendMessage(msg);
    }
    
	private class ConnectThread extends Thread{
		
		private final BluetoothDevice device;
		private final BluetoothSocket socket;
		
		public ConnectThread(BluetoothDevice device){
			this.device = device;
			BluetoothSocket tmpSocket = null;
			
			 // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try{
            	tmpSocket = device.createRfcommSocketToServiceRecord(BluetoothProperties.EBIKE_UUID);
            }
            catch (IOException e) {
                Log.e(Tags.LOG_TAG, "create() failed", e);
            }
            
            this.socket = tmpSocket;
		}
		
		@Override
		public void run(){
			Log.i(Tags.LOG_TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            if(btAdapter.isDiscovering())
            	btAdapter.cancelDiscovery();
            
         // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket.connect();
            } catch (IOException e) {
                connectionFailed();
                // Close the socket
                try {
                    socket.close();
                } catch (IOException e2) {
                    Log.e(Tags.LOG_TAG, "unable to close() socket during connection failure", e2);
                }
                // Start the service over to restart listening mode
                //BluetoothSerialService.this.start();
                return;
            }
            
            // Reset the ConnectThread because we're done
            synchronized (BluetoothService.this) {
                connectThread = null;
            }

            // Start the connected thread
            connected(socket, device);
		}
		
		public void cancel(){
			try {
                socket.close();
            } catch (IOException e) {
                Log.e(Tags.LOG_TAG, "close() of connect socket failed", e);
            }
		}
	}
	private class ConnectedThread extends Thread{
		
		private final BluetoothSocket socket;
		private final InputStream in;
		private final OutputStream out;
		
		private ReceiveListener receiveListener;
		
		public ConnectedThread(BluetoothSocket socket, ReceiveListener receiveListener){
			Log.d(Tags.LOG_TAG, "create ConnectedThread");
			
			this.socket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            this.receiveListener = receiveListener;
            
            
            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = this.socket.getInputStream();
                tmpOut = this.socket.getOutputStream();
            } catch (IOException e) {
                Log.e(Tags.LOG_TAG, "temp sockets not created", e);
            }
            
            in = tmpIn;
            out = tmpOut;
		}
		
		@Override
		public void run(){
			Log.i(Tags.LOG_TAG, "BEGIN connectedThread");
            byte[] frame = new byte[1024];
            int frameLength = 0;
            int startByte;
            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                	if(!STARTBYTE_RECEIVED){
                		startByte = in.read();
                		if(startByte == 0xAA){
                			STARTBYTE_RECEIVED = true;
                		}
                	}
                	if(STARTBYTE_RECEIVED){
                		frameLength = in.read();
                		LENGTH_RECEIVED = true;
                	}
                	if(LENGTH_RECEIVED){
                		in.read(frame, 0, frameLength-1);
                		receiveListener.onNewFrameReceived(frame, frameLength);
                		LENGTH_RECEIVED = false;
                		STARTBYTE_RECEIVED = false;
                	}
                		
                	
                    //TODO:
                    
                    // Send the obtained bytes to the UI Activity
                    //mHandler.obtainMessage(BlueTerm.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    
                } catch (IOException e) {
                    Log.e(Tags.LOG_TAG, "disconnected", e);
                    connectionLost();
                    break;
                }
            }
		}
		
		/**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void write(byte[] frame) {
            try {
                out.write(frame);
                out.flush();
                Log.d(Tags.LOG_TAG, "Message send to BT Device");
                //TODO:
                // Share the sent message back to the UI Activity
//                mHandler.obtainMessage(BlueTerm.MESSAGE_WRITE, frame.length, -1, frame)
//                        .sendToTarget();
                
            } catch (IOException e) {
                Log.e(Tags.LOG_TAG, "Exception during write", e);
            }
        }
		public void cancel(){
			try {
                socket.close();
            } catch (IOException e) {
                Log.e(Tags.LOG_TAG, "close() of connect socket failed", e);
            }
		}
	}
}
