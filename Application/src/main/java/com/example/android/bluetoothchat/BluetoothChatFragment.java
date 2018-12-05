/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.bluetoothchat;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.android.common.logger.Log;

/**
 * This fragment controls Bluetooth to communicate with other devices.
 */
public class BluetoothChatFragment extends Fragment {

    private static final String TAG = "BluetoothChatFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Layout Views
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;

    private FoundationPile fOne;
    private FoundationPile fTwo;
    private FoundationPile fThree;
    private FoundationPile fFour;
    private FoundationPile fFive;
    private FoundationPile fSix;
    private FoundationPile fSeven;
    private FoundationPile fEight;

    private AtomicInteger userTwoStuck;
    private AtomicInteger userTwoDone;
    private AtomicInteger userTwoNerts;
    private AtomicInteger userTwoScore;

    private AtomicInteger otherRandNum;

    ArrayList<ImageView> dealerImages = new ArrayList<ImageView>();
    ArrayList<ImageView> userOneImages = new ArrayList<ImageView>();
    ArrayList<ImageView> userTwoImages = new ArrayList<ImageView>();

    int dealerCount = 0;
    int userOneCount = 0;
    int userTwoCount = 0;

    BlackjackActivity table;

    public void setBJActivity(BlackjackActivity temp)
    {
        table = temp;
    }

    public void setBJVars(AtomicInteger temp, int i)
    {
        if(i == 1)
        {
            otherRandNum = temp;
        }

    }

    public void setBJViews(ArrayList<ImageView> temp, int i)
    {
        if(i == 1)
        {
            dealerImages = temp;
        }
        else if(i == 2)
        {
            userOneImages = temp;
        }
        else if(i == 3)
        {
            userTwoImages = temp;
        }
    }

    public void setNertzBooleans(AtomicInteger temp, int i)
    {
        if(i == 1)
        {
            userTwoStuck = temp;
        }
        else if(i == 2)
        {
            userTwoDone = temp;
        }
        else if(i == 3)
        {
            userTwoNerts = temp;
        }
        else if(i == 4)
        {
            userTwoScore = temp;
        }
    }

    public void setFoundations(FoundationPile pile, int i)
    {
        if(i == 1)
        {
            fOne = pile;
        }
        else if(i == 2)
        {
            fTwo = pile;
        }
        else if(i == 3)
        {
            fThree = pile;
        }
        else if(i == 4)
        {
            fFour = pile;
        }
        else if(i == 5)
        {
            fFive = pile;
        }
        else if(i == 6)
        {
            fSix = pile;
        }
        else if(i == 7)
        {
            fSeven = pile;
        }
        else if(i == 8)
        {
            fEight = pile;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bluetooth_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mConversationView = (ListView) view.findViewById(R.id.in);
        mOutEditText = (EditText) view.findViewById(R.id.edit_text_out);
        mSendButton = (Button) view.findViewById(R.id.button_send);
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

        mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                    TextView textView = (TextView) view.findViewById(R.id.edit_text_out);
                    String message = textView.getText().toString();
                    sendMessage(message);
                }
            }
        });

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(getActivity(), mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }

    /**
     * The action listener for the EditText widget, to listen for the return key
     */
    private TextView.OnEditorActionListener mWriteListener
            = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    processJSON(readMessage);
                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

    /**
     * Establish connection with other device
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bluetooth_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }

    private void processJSON(String message)
    {
        try
        {
            JSONObject obj = new JSONObject(message);
            if(obj.getString("Game").equals("Nertz"))
            {
                if(obj.getString("Pile").equals("1"))
                {
                    int card = Integer.parseInt(obj.getString("Card"));
                    int suit = card / 13;
                    int face = card - (suit * 13);
                    Card newCard = new Card(face, suit, 0);
                    fOne.addCard(newCard);
                    fOne.showTop();
                }
                else if(obj.getString("Pile").equals("2"))
                {
                    int card = Integer.parseInt(obj.getString("Card"));
                    int suit = card / 13;
                    int face = card - (suit * 13);
                    Card newCard = new Card(face, suit, 0);
                    fTwo.addCard(newCard);
                    fTwo.showTop();
                }
                else if(obj.getString("Pile").equals("3"))
                {
                    int card = Integer.parseInt(obj.getString("Card"));
                    int suit = card / 13;
                    int face = card - (suit * 13);
                    Card newCard = new Card(face, suit, 0);
                    fThree.addCard(newCard);
                    fThree.showTop();
                }
                else if(obj.getString("Pile").equals("4"))
                {
                    int card = Integer.parseInt(obj.getString("Card"));
                    int suit = card / 13;
                    int face = card - (suit * 13);
                    Card newCard = new Card(face, suit, 0);
                    fFour.addCard(newCard);
                    fFour.showTop();
                }
                else if(obj.getString("Pile").equals("5"))
                {
                    int card = Integer.parseInt(obj.getString("Card"));
                    int suit = card / 13;
                    int face = card - (suit * 13);
                    Card newCard = new Card(face, suit, 0);
                    fFive.addCard(newCard);
                    fFive.showTop();
                }
                else if(obj.getString("Pile").equals("6"))
                {
                    int card = Integer.parseInt(obj.getString("Card"));
                    int suit = card / 13;
                    int face = card - (suit * 13);
                    Card newCard = new Card(face, suit, 0);
                    fSix.addCard(newCard);
                    fSix.showTop();
                }
                else if(obj.getString("Pile").equals("7"))
                {
                    int card = Integer.parseInt(obj.getString("Card"));
                    int suit = card / 13;
                    int face = card - (suit * 13);
                    Card newCard = new Card(face, suit, 0);
                    fSeven.addCard(newCard);
                    fSeven.showTop();
                }
                else if(obj.getString("Pile").equals("8"))
                {
                    int card = Integer.parseInt(obj.getString("Card"));
                    int suit = card / 13;
                    int face = card - (suit * 13);
                    Card newCard = new Card(face, suit, 0);
                    fEight.addCard(newCard);
                    fEight.showTop();
                }
                else if(obj.getString("Stuck").equals("1"))
                {
                    userTwoStuck.set(1);
                }
                else if(obj.getString("Stuck").equals("0"))
                {
                    userTwoStuck.set(0);
                }
                else if(obj.getString("Done").equals("1"))
                {
                    userTwoDone.set(1);
                    userTwoScore.set(Integer.parseInt(obj.getString("Score")));
                }
                else if(obj.getString("Done").equals("0"))
                {
                    userTwoDone.set(0);
                }
                else if(obj.getString("Nertz").equals("1"))
                {
                    userTwoNerts.set(1);
                    userTwoScore.set(Integer.parseInt(obj.getString("Score")));
                }
            }
            else
            {
                if(!obj.getString("RandNum").equals(""))
                {
                    otherRandNum.set(Integer.parseInt(obj.getString("RandNum")));
                }
                else if(obj.getString("State").equals("0"))
                {
                    int card = Integer.parseInt(obj.getString("Card"));
                    int suit = card / 13;
                    int face = card - (suit * 13);
                    Card newCard = new Card(face, suit, 0);

                    if(obj.getString("User").equals("1"))
                    {
                        String path = "card" + newCard.toString();
                        int imageCard = table.getResources().getIdentifier(path, "drawable", table.getPackageName());
                        userOneImages.get(userOneCount).setImageResource(imageCard);
                        userOneCount++;
                    }
                    else if(obj.getString("User").equals("2"))
                    {
                        String path = "card" + newCard.toString();
                        int imageCard = table.getResources().getIdentifier(path, "drawable", table.getPackageName());
                        userTwoImages.get(userTwoCount).setImageResource(imageCard);
                        userTwoCount++;
                    }
                    else
                    {
                        String path = "card" + newCard.toString();
                        int imageCard = table.getResources().getIdentifier(path, "drawable", table.getPackageName());
                        dealerImages.get(dealerCount).setImageResource(imageCard);
                        dealerCount++;
                    }
                }
            }
        }
        catch (JSONException e)
        {

        }
    }

}
