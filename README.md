# Hotel Management System - Professional Enterprise Application

A comprehensive hotel management solution built with Java Swing and MongoDB, featuring role-based authentication, modern UI/UX design, and complete administrative tools for managing staff, rooms, pricing, and financial reporting. This enterprise-grade application provides separate dashboards for administrators and receptionists, with secure database integration and professional-grade styling.

## 🏗️ Project Structure

```
hotel-management-system/
├── pom.xml                                 
├── .env                                    
├── lib/                                    
├── src/
│   ├── main/
│   │   ├── java/com/hotel/management/
│   │   │   ├── Main.java                   
│   │   │   ├── UserCreationScript.java     
│   │   │   ├── database/                   
│   │   │   │   └── DatabaseConnection.java
│   │   │   ├── model/                      
│   │   │   │   ├── User.java              
│   │   │   │   ├── Guest.java
│   │   │   │   ├── GuestActivity.java
│   │   │   │   ├── FinancialRecord.java
│   │   │   │   ├── Receptionist.java
│   │   │   │   └── ReceptionistStaff.java
│   │   │   ├── service/                   
│   │   │   │   ├── UserService.java        
│   │   │   │   ├── GuestActivityManagement.java
│   │   │   │   └── ReceptionistStaffManagement.java
│   │   │   └── view/                      
│   │   │       ├── LoginSelectionFrame.java
│   │   │       ├── admin/                  
│   │   │       │   ├── AdminLoginFrame.java
│   │   │       │   ├── AdminDashboardFrame.java
│   │   │       │   ├── AdminSettingsPanel.java
│   │   │       │   ├── AddStaffPanel.java
│   │   │       │   ├── RemoveStaffPanel.java
│   │   │       │   ├── ViewStaffPanel.java
│   │   │       │   ├── AddRoomPanel.java
│   │   │       │   ├── PriceAdjustmentPanel.java
│   │   │       │   ├── FinancePanel.java
│   │   │       │   ├── ViewReportsPanel.java
│   │   │       │   ├── DailyReportTablePanel.java
│   │   │       │   └── GuestActivityPanel.java
│   │   │       ├── receptionist/           
│   │   │       │   ├── ReceptionistLoginFrame.java
│   │   │       │   ├── ReceptionistDashboardFrame.java
│   │   │       │   ├── ReceptionistPanel.java
│   │   │       │   ├── Receptionistcheckins.java
│   │   │       │   └── ReceptionistCheckouts.java
│   │   │       └── components/             
## 🚀 How to Run

### Prerequisites

1. **MongoDB** must be installed and running locally on port `27017`
2. **Java 17+** installed
3. **Maven** installed (or use IntelliJ's built-in Maven)

### Initial Setup

1. **Configure MongoDB Connection**:
   - Create/edit `.env` file in project root:
   ```env
   MONGODB_URI=mongodb://localhost:27017
   MONGODB_DATABASE=hotel_management_db
   ```

2. **Create Users** (First Time Only):
   ```powershell

   mvn compile exec:java -Dexec.mainClass="com.hotel.management.UserCreationScript"

## 🔑 Login Credentials

**Important**: Login credentials are now stored securely in MongoDB.
**Default Users** (if created via script):
- **Admin**: Create via script with role "admin"
- **Receptionist**: Create via script with role "receptionist"

Users are authenticated against MongoDB with hashed passwords. on `Main.java` → **Run 'Main.main()'**
## 🚀 How to Run

### Option 1: Using IntelliJ IDEA (Recommended)

1. **Open the project** in IntelliJ IDEA
2. IntelliJ will automatically detect the `pom.xml` and configure as Maven project
3. Wait for Maven to download dependencies (JCalendar)
4. **Right-click** on `Main.java` → **Run 'Main.main()'**

## 📦 Dependencies

- **Java**: 17+
- **MongoDB Driver**: 4.11.1 (database connectivity)
- **Dotenv Java**: 3.0.0 (environment variable management)
- **JCalendar**: 1.4 (date picker component)

## 🎯 Features

### Authentication & Security
- **MongoDB Integration**: Secure user authentication with database persistence
- **Role-Based Access**: Separate admin and receptionist roles
- **User Management**: CLI script for creating and managing users

### Admin Dashboard
- **Professional Sidebar**: Gradient backgrounds, smooth navigation, collapsible menus
- **Staff Management**: Add, remove, and view staff with department/role filtering
- **Room Management**: Update room status with custom styled dropdowns
- **Price Adjustment**: Seasonal pricing controls with date pickers
- **Finance Tracking**: Revenue reports and financial metrics
- **View Reports**: Occupancy rates, revenue analytics, staff efficiency
- **Settings Panel**: User profile management

### Receptionist Dashboard
- **Guest Check-ins/Check-outs**: Streamlined guest management
- **Room Availability**: Real-time room status tracking
- **Guest Activity**: Monitor guest interactions and services

### UI/UX Enhancements
- **Modern Dark Theme**: Gold (#FFB43C) accent on dark backgrounds
- **Custom Calendar Components**: Interactive date pickers with month navigation
- **Styled Dropdowns**: White backgrounds with black text, gold selection highlights
- **Responsive Design**: Professional gradient effects, shadows, and borders
- **Navigation State Tracking**: Visual feedback for current selection

### Technical Features
- **MVC Architecture**: Clean separation of models, services, and views
- **Maven Build System**: Automated dependency management
## 🔧 Development

### IntelliJ Setup

1. **File → Project Structure**
   - Project SDK: Java 17
   - Language Level: 17
2. **Mark directories**:
   - `src/main/java` → Sources
   - `src/main/resources` → Resources
   - `src/test/java` → Test Sources
3. **Configure .env file**:
   - Create `.env` in project root
   - Add MongoDB connection details

### Building

```powershell
# Compile only
mvn compile

# Run user creation script
mvn compile exec:java -Dexec.mainClass="com.hotel.management.UserCreationScript"

# Package as JAR with dependencies
## 📝 Notes

- Old source files remain in `src/` directory for backup
- New structure is in `src/main/java/com/hotel/management/`
- All image paths updated from `/Rescources/` to `/images/`
- Maven handles all dependencies automatically
- MongoDB must be running before starting the application
- User passwords are hashed before storage in database

## 🎨 Recent Updates

### Database & Authentication (December 2025)
✅ **MongoDB Integration**: Complete authentication system with secure database storage  
✅ **User Service**: UserService.java with authentication, user creation, and management  
✅ **Environment Configuration**: .env file for flexible database configuration  
✅ **User Creation Tool**: Interactive CLI script for creating admin/receptionist accounts  
✅ **Login Screens Updated**: Both admin and receptionist logins now authenticate against MongoDB  

### UI/UX Improvements (December 2025)
✅ **Professional Sidebar**: Gradient backgrounds, navigation state tracking, auto-collapsing dropdowns  
✅ **Dropdown Visibility Fix**: All combobox dropdowns now show black text on white background  
✅ **Calendar Components**: Custom date pickers with month navigation in all panels  
✅ **Settings Panel**: AdminSettingsPanel with proper text field selection colors  
✅ **View Reports Panel**: Multi-tab reporting interface with filters and metrics  
✅ **Consistent Styling**: Gold accent color (#FFB43C) across all components  

### Architecture Enhancements
✅ **Professional package structure** (`com.hotel.management.*`)  
✅ **Maven build system** (pom.xml)  
✅ **MVC Pattern**: Proper separation of model, service, and view layers  
✅ **Singleton Database Connection**: Efficient connection pooling  
✅ **Resource management** (`src/main/resources`)  
✅ **Automated dependency management**  

## 🛠️ Troubleshooting

### MongoDB Connection Issues
- Ensure MongoDB is running: `net start MongoDB`
- Check `.env` file has correct connection string
- Verify MongoDB is listening on port 27017

### Login Issues
- Run `UserCreationScript.java` to create users
- Verify users exist in MongoDB: `db.users.find()`
- Check role matches ("admin" or "receptionist")

### Dropdown Visibility Issues
- All dropdowns fixed to show black text on white background
- If issues persist, verify Java version is 17+

---

**Version**: 2.0.0  
**Last Updated**: December 3, 2025
2. **Create Database** (automatic on first run):
   - Database: `hotel_management_db`
   - Collection: `users`

3. **Create Users**:
   ```powershell
   mvn compile exec:java -Dexec.mainClass="com.hotel.management.UserCreationScript"
   ```
- **Admin Dashboard**: Staff management, room status, pricing, finance tracking
- **Receptionist Dashboard**: Check-ins, check-outs, guest management
- **Modern UI**: Custom Swing components with dark theme
- **Professional Structure**: Proper package organization following MVC pattern

## 🔧 Development

### IntelliJ Setup

1. **File → Project Structure**
   - Project SDK: Java 17
   - Language Level: 17
2. **Mark directories**:
   - `src/main/java` → Sources
   - `src/main/resources` → Resources
   - `src/test/java` → Test Sources

### Building

```powershell
# Compile only
mvn compile

# Package as JAR with dependencies
mvn clean package

# Skip tests (when added)
mvn package -DskipTests
```
