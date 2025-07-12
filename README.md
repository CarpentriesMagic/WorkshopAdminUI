# README

This Java program provides a graphical user interface (GUI) for the workshop
management system that makes use of a database. It keeps track of workshops,
helpers, instructors and rooms. If kept up to date, it can be used to use
the CarpentriesMagic bash script to automatically generate a workshop website.

The repository for the bash script can be found here: https://github.com/CarpentriesMagic/WorkshopAdmin

# Architecture

```mermaid

classDiagram

    Helpers *-- Helper
    HelperTableModel *-- Helpers
    HelperTable *-- HelperTableModel
    HelperPanel *-- HelperTable

    Instructors *-- Instructor
    InstructorTableModel *-- Instructors
    InstructorTable *-- InstructorTableModel
    InstructorPanel *-- InstructorTable

    People *-- Person
    PersonTableModel *-- People
    PersonTable *-- PersonTableModel
    PersonPanel *-- PersonTable

```

```mermaid

classDiagram
    Rooms *-- Room
    RoomTableModel *-- Rooms
    RoomTable *-- RoomTableModel
    RoomPanel *-- RoomTable
    
    Settings *-- Setting
    SettingTableModel *-- Settings
    SettingTable *-- SettingTableModel
    SettingPanel *-- SettingTable

    Workshops *-- Workshop
    WorkshopTableModel *-- Workshops
    WorkshopTable *-- WorkshopTableModel
    WorkshopPanel *-- WorkshopTable
    WorkshopEntryPanel *-- WorkshopEntry
    SplitPane *-- WorkshopTablePanel
    SplitPane *-- WorkshopEntryPanel
    WorkshopPanel *-- SplitPane
```
```mermaid
classDiagram
    DBHandler <|-- MySQLDBHandler
    
    MainFrame *-- MainTabbedPane
```