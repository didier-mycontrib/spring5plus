pour que mapStruct s'intégre bien à eclipse:

1) installer le plugin m2e-apt via Help / Eclipse marketPlace

2) project/properties .../ maven /annotation processing /enable specific / automatically configure JDT APT

=======================
ordre de bonne reconstruction:

1) run as maven build ...   clean package  (et skipTest)

2) project/clean 

3) run as / java application (ou junit test)