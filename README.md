# README

```mermaid

classDiagram
    
    Settings *-- Setting
    SettingTableModel *-- Settings
    SettingTable *-- SettingTableModel


    DBHandler <|-- MySQLDBHandler
```