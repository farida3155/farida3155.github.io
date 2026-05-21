# 🌐 Enterprise Network Design & Implementation (Packet Tracer)

A complete enterprise-level network simulation built using Cisco Packet Tracer. This project demonstrates advanced networking concepts including VLSM addressing, routing protocols, NAT, VPN, DHCP, and network security.

---

## 📌 Overview

This project simulates a real-world multi-branch organizational network. The system was designed to ensure:

* Full connectivity across multiple locations
* Efficient IP addressing using VLSM
* Secure communication using IPsec VPN
* Automated host configuration via DHCP
* Scalable and maintainable network architecture

---

## 🚀 Key Features

### 🌍 Network Architecture

* Multi-branch network design (Main, S, N, R branches)
* VLAN-based segmentation for different departments
* Interconnected routers, multilayer switches, and end devices

---

### 📊 VLSM Addressing Scheme

* Designed using network: `10.1.0.0/12`
* Efficient subnet allocation based on host requirements
* Minimizes IP wastage and supports scalability

---

### 🔀 Routing & Connectivity

* **OSPF (Single Area)** for core routing
* **EIGRP** for branch routing
* **Route Redistribution** between OSPF and EIGRP
* Static default routes to ISP

---

### 🔌 Layer 2 Configuration

* VLAN creation and management
* 802.1Q trunking between switches
* Inter-VLAN routing using multilayer switches
* EtherChannel (LACP) for link aggregation

---

### 📡 DHCP Configuration

* Centralized DHCP server for automatic IP assignment
* DHCP relay agents configured on L3 switches
* Branch router configured as DHCP server

---

### 🌐 NAT & Internet Access

* Dynamic NAT with PAT for internal users
* Static NAT for public server access (miu.edu.eg)
* Connectivity with ISP simulated network

---

### 🔐 Network Security

* Site-to-Site IPsec VPN between branches
* Secure communication across networks
* SSH configuration for secure device access

---

### 🛠️ Network Services

* DNS Server (domain resolution)
* Web Server (miu.edu.eg)
* Email Server configuration
* NTP & Syslog for network management

---

### 📶 Wireless Network

* Configured wireless home router
* WPA2 security setup
* Multiple wireless clients (Laptop, Tablet, Smartphone)

---

### 📈 Network Verification

* End-to-end connectivity testing using ping
* Routing table verification
* DHCP assignment validation
* NAT translation checks

---

## 📸 Screenshots

Include the following:

<img width="1331" height="587" alt="Screenshot 2026-05-05 204155" src="https://github.com/user-attachments/assets/b53fee2b-2aa2-4c73-a666-19c75ccb91fb" /> <br><br>
<img width="545" height="222" alt="pc4 to email server" src="https://github.com/user-attachments/assets/00d16aa1-c8cf-452c-84fb-0f55e7b9ee4a" /> <br><br>
<img width="846" height="581" alt="show ip route" src="https://github.com/user-attachments/assets/afe8eb02-7dab-4b59-9237-d7585bccb0c7" /> <br><br>
<img width="1440" height="809" alt="Screenshot 2026-05-05 204705" src="https://github.com/user-attachments/assets/5e328663-7370-43d4-9c03-f2b1a50ce301" />





---

## 🛠️ Technologies & Tools

* Cisco Packet Tracer
* Networking Protocols: OSPF, EIGRP
* NAT (PAT & Static NAT)
* IPsec VPN
* VLANs & Trunking
* DHCP, DNS, NTP, Syslog

---

## 📁 Project Structure

```
network-project/
├── network-topology.pkt
└── README.md
```

---

## 🧠 Key Learning Outcomes

* Designing scalable networks using VLSM
* Configuring dynamic routing protocols
* Implementing secure communication (VPN)
* Managing IP addressing and automation (DHCP)
* Understanding real-world enterprise network architecture

---

## 📌 Notes

This project was developed as part of the Computer Networks course and demonstrates practical implementation of networking concepts in a simulated environment.
