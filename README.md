# Hotel Management System

A modern, Java Swing-based desktop application for hotel administration. This dashboard provides a sleek, dark-themed interface for managing staff, room status, and pricing adjustments.

## Features

### Admin Login
- Secure login screen with modern styling

### Interactive Dashboard
- Visual circular charts for staff statistics
- Summary cards for Users, Rooms, and Bookings

### Manage Staff
- **Add Staff**: Form with auto-generated IDs and custom calendar date picker
- **Delete Staff**: Form to look up and remove staff members
- **View Staff**: Tabular view of all staff with delete actions

### Manage Rooms
- **Update Status**: Check room status (Occupied/Available/Cleaning) and assign staff tasks
- **Price Adjustment**: Calculate seasonal price changes based on percentage increase
- **Update Rooms**: Placeholder for future room modification features

### Custom UI Components
- Transparent panels with blur-like effects
- Custom-painted buttons and dropdowns
- Vector-based icons drawn programmatically

## Prerequisites

- **Java Development Kit (JDK)**: Version 8 or higher
- **IDE (Optional)**: IntelliJ IDEA, Eclipse, or NetBeans recommended

## Project Structure

To run this application successfully, ensure your project folder looks exactly like this:

```
ProjectRoot/
│
├── src/
│   ├── AdminLoginFrame.java        
│   ├── AdminDashboardFrame.java    
│   ├── AddStaffPanel.java          
│   ├── RemoveStaffPanel.java       
│   ├── ViewStaffPanel.java        
│   ├── AddRoomPanel.java           
│   ├── PriceAdjustmentPanel.java   
│   ├── CircleProgressPanel.java    
│   │
│   └── Rescources               
```

## Setup & Installation

1. **Clone or Download** the repository

2. **Create the Resources Folder**:
   - Inside your source folder (`src`), create a folder named `Rescources`
   - ⚠️ **Note**: The code uses the specific spelling `Rescources`. If you change this to `resources`, you must update all file paths in the Java files

3. **Add Images**:
   - Place your background image (rename it to `a170b7e6b576d72403c665e6337322e1.jpg` or update the code)
   - Place your logo image as `logo.png`
   - Place your login icon (rename to `b41ff478e42e31fd71584d9dae338ffa-removebg-preview.png` or update the code)

4. **Compile and Run**:
   - Compile all `.java` files
   - Run `AdminLoginFrame` as the main class

## Credentials

- **Username**: `admin`
- **Password**: `temp123`

## Customization

- **Colors**: Edit the `GOLD_COLOR` and `FORM_BACKGROUND` constants in any panel class to change the theme
- **Images**: Update the file paths in the try-catch blocks within `AdminLoginFrame` and `AdminDashboardFrame` to use your own assets

## Troubleshooting

- **"Background image not found"**: Ensure the `Rescources` folder is in the correct location relative to your compiled class files (classpath). In many IDEs, this folder should be under `src`
- **UI Glitches**: This app uses custom transparency. Ensure your graphics drivers are up to date. The transparency effect relies on proper graphics rendering


**Developed with ☕ and Java Swing**
