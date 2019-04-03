package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.DatagramSocketImpl;
import java.net.DatagramSocketImplFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button ipBtn = null, connBtn = null;
    String myIP = null;
    String bIP = null;
    Socket socket = null;
    TextView textView = null;
    OutputStream os = null;
    InputStream is = null;
    DatagramSocket datagramSocket = null;
    Button clientA = null;
    Button myP2PConnect = null;
    DatagramSocket connectDataSocket = null;
    volatile boolean isRead = false;
    PeerInfo destinationInfo = null;
    InetSocketAddress destinationAddress = null;
    DatagramSocket destionationSocket = null;
    Button desbtn = null;
    InetSocketAddress serverAddress = null;
    public MainActivity() throws SocketException {
        // datagramSocket.setReuseAddress(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initBtnClick();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
//            actionIntent intent = new Intent();
//
//            startActivity();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initBtnClick() {
        ipBtn = findViewById(R.id.ip_btn);
        connBtn = findViewById(R.id.conn_btn);
        Button needBtn = findViewById(R.id.need_btn);
        clientA = findViewById(R.id.ClientA);
        myP2PConnect = findViewById(R.id.connection);
        textView = findViewById(R.id.user);
        desbtn = findViewById(R.id.destination);
        if (ipBtn == null || connBtn == null) {
            ipBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "按钮无法点击", Toast.LENGTH_LONG);
                }
            });
        } else {
            // 点击获取ip
            ipBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (datagramSocket == null) {
                                    datagramSocket = new DatagramSocket(8888);
                                    datagramSocket.setReuseAddress(true);
                                }
                                if (socket == null) socket = new Socket("dyzhello.club", 7777);
                                os = socket.getOutputStream();
                                is = socket.getInputStream();
                                String text = (String) textView.getText();
                                os.write("B".getBytes());
                                os.flush();

                                byte[] bytes = new byte[1024];
                                StringBuilder sb = new StringBuilder();

                                int number = is.read(bytes);

                                sb.append(new String(bytes, 0, number, "utf-8"));

                                myIP = sb.toString();
                                //datagramSocket.bind(new InetSocketAddress("localhost", 8888));
                                System.out.append(sb.toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
            // 点击创建连接
            connBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sendAndRead();

                }
            });

            needBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String name = (String) textView.getText();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                os.write(("GET A").getBytes());
                                os.flush();
                                byte[] bytes = new byte[1024];
                                int number = 0;
                                number = is.read(bytes);
                                System.out.println(new String(bytes));
                                bIP = new String(bytes, 0, number, "utf-8");
                                datagramSocket.connect(new InetSocketAddress(bIP, 8888));
                                textView.setText(bIP);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
        }
        clientA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UDPClientA.AMAIN();
                    }
                }).start();
            }
        });
        myP2PConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        serverAddress = new InetSocketAddress("dyzhello.club", 7777);
                        try {
                            PeerMessage peerMessage = new PeerMessage();
                            peerMessage.setType(PeerMessage.TYPE_REGISTER);
                            peerMessage.setIntranetIP(InetAddress.getLocalHost().toString());
                            peerMessage.setName("AAA");
                            if (connectDataSocket == null) connectDataSocket = new DatagramSocket();
                            String requestJson = JSON.toJSONString(peerMessage);
                            DatagramPacket registerRequest = new DatagramPacket(requestJson.getBytes(), requestJson.length(), serverAddress);
                            connectDataSocket.send(registerRequest);
                            DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
                            while (true) {
                                connectDataSocket.receive(response);
                                //  解析报文
                                PeerResponseMessage responseMessage = JSON.parseObject(new String(response.getData(), 0, response.getLength(), "utf-8")).toJavaObject(PeerResponseMessage.class);
                                destinationInfo.setIntraNetIP(responseMessage.getDestinationIP());
                                destinationInfo.setPort(responseMessage.getDestinationPort());
                                destinationAddress = new InetSocketAddress(destinationInfo.getNatIP(), destinationInfo.getPort());
                                if (responseMessage.getMsg().equals("destinationInfo")) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String linkComd = "link";
                                            while (true) {
                                                try {
                                                    DatagramPacket linkPacket = new DatagramPacket(linkComd.getBytes(), linkComd.length(), destinationAddress);
                                                    connectDataSocket.send(linkPacket);
                                                    Thread.sleep(3000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }).start();
                                }
                                if (responseMessage.getMsg().equals("assistInfo")) {
                                    String linkComd = "assist";
                                    DatagramPacket linkPacket = new DatagramPacket(linkComd.getBytes(), linkComd.length(), destinationAddress);
                                    connectDataSocket.send(linkPacket);
                                    new Thread(new Runnable() {
                                        DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
                                        @Override
                                        public void run() {
                                            while (true) {
                                                try {
                                                    connectDataSocket.receive(receivePacket);
                                                    String responseStr = new String(receivePacket.getData(), 0, receivePacket.getLength(), "utf-8");
                                                    System.out.println(responseStr);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }).start();
                                }
                            }
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (SocketException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        desbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PeerMessage request = new PeerMessage();
                        request.setType(PeerMessage.TYPE_ASSIST);
                        request.setName("BBB");
                        String requestJson = JSON.toJSONString(request);
                        DatagramPacket requestPacket = new DatagramPacket(requestJson.getBytes(), requestJson.length(), serverAddress);
                        try {
                            connectDataSocket.send(requestPacket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
    public void sendAndRead() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isRead)
                try {
                    String handshakeMsg = "PC_Message";

                    DatagramPacket packet = new DatagramPacket(handshakeMsg.getBytes(), handshakeMsg.getBytes().length, new InetSocketAddress(bIP, 8888));
                    datagramSocket.send(packet);
                    Thread.sleep(5000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes = new byte[1024];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(bytes, 1024);
                    try {
                        datagramSocket.receive(packet);
                        if (!isRead) isRead = true;
                        textView.append(new String(packet.getData()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }
}
