package com.hotel.management.service;

import com.hotel.management.model.Ticket;
import com.hotel.management.util.DatabaseConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TicketService {
    
    private final MongoCollection<Document> ticketCollection;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public TicketService() {
        MongoDatabase database = DatabaseConnection.getDatabase();
        this.ticketCollection = database.getCollection("support_tickets");
    }
    
    private String generateTicketId() {
        long count = ticketCollection.countDocuments() + 1;
        String year = new SimpleDateFormat("yyyy").format(new Date());
        return String.format("TKT-%s-%04d", year, count);
    }
    
    public boolean createTicket(Ticket ticket) {
        try {
            ticket.setTicketId(generateTicketId());
            ticket.setCreatedAt(new Date());
            ticket.setStatus("Open");
            
            Document doc = new Document()
                    .append("ticketId", ticket.getTicketId())
                    .append("title", ticket.getTitle())
                    .append("description", ticket.getDescription())
                    .append("category", ticket.getCategory())
                    .append("priority", ticket.getPriority())
                    .append("status", ticket.getStatus())
                    .append("roomNumber", ticket.getRoomNumber())
                    .append("guestName", ticket.getGuestName())
                    .append("createdBy", ticket.getCreatedBy())
                    .append("createdAt", ticket.getCreatedAt())
                    .append("assignedTo", ticket.getAssignedTo())
                    .append("resolvedAt", ticket.getResolvedAt())
                    .append("adminNotes", ticket.getAdminNotes());
            
            ticketCollection.insertOne(doc);
            System.out.println("✓ Ticket created: " + ticket.getTicketId());
            return true;
        } catch (Exception e) {
            System.err.println("✗ Error creating ticket: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        try {
            for (Document doc : ticketCollection.find().sort(Sorts.descending("createdAt"))) {
                tickets.add(documentToTicket(doc));
            }
        } catch (Exception e) {
            System.err.println("✗ Error fetching tickets: " + e.getMessage());
            e.printStackTrace();
        }
        return tickets;
    }
    
    public List<Ticket> getTicketsByStatus(String status) {
        List<Ticket> tickets = new ArrayList<>();
        try {
            for (Document doc : ticketCollection.find(Filters.eq("status", status))
                    .sort(Sorts.descending("createdAt"))) {
                tickets.add(documentToTicket(doc));
            }
        } catch (Exception e) {
            System.err.println("✗ Error fetching tickets by status: " + e.getMessage());
            e.printStackTrace();
        }
        return tickets;
    }
    
    public List<Ticket> getTicketsByCreator(String createdBy) {
        List<Ticket> tickets = new ArrayList<>();
        try {
            for (Document doc : ticketCollection.find(Filters.eq("createdBy", createdBy))
                    .sort(Sorts.descending("createdAt"))) {
                tickets.add(documentToTicket(doc));
            }
        } catch (Exception e) {
            System.err.println("✗ Error fetching tickets by creator: " + e.getMessage());
            e.printStackTrace();
        }
        return tickets;
    }
    
    public boolean updateTicketStatus(String ticketId, String status, String assignedTo) {
        try {
            Document update = new Document("$set", new Document()
                    .append("status", status)
                    .append("assignedTo", assignedTo));
            
            if (status.equals("Resolved") || status.equals("Closed")) {
                update.get("$set", Document.class).append("resolvedAt", new Date());
            }
            
            ticketCollection.updateOne(Filters.eq("ticketId", ticketId), update);
            System.out.println("✓ Ticket " + ticketId + " status updated to: " + status);
            return true;
        } catch (Exception e) {
            System.err.println("✗ Error updating ticket status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean addAdminNotes(String ticketId, String notes) {
        try {
            Document update = new Document("$set", new Document("adminNotes", notes));
            ticketCollection.updateOne(Filters.eq("ticketId", ticketId), update);
            System.out.println("✓ Admin notes added to ticket: " + ticketId);
            return true;
        } catch (Exception e) {
            System.err.println("✗ Error adding admin notes: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteTicket(String ticketId) {
        try {
            ticketCollection.deleteOne(Filters.eq("ticketId", ticketId));
            System.out.println("✓ Ticket deleted: " + ticketId);
            return true;
        } catch (Exception e) {
            System.err.println("✗ Error deleting ticket: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public Ticket getTicketById(String ticketId) {
        try {
            Document doc = ticketCollection.find(Filters.eq("ticketId", ticketId)).first();
            if (doc != null) {
                return documentToTicket(doc);
            }
        } catch (Exception e) {
            System.err.println("✗ Error fetching ticket: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public long getTicketCountByStatus(String status) {
        try {
            return ticketCollection.countDocuments(Filters.eq("status", status));
        } catch (Exception e) {
            System.err.println("✗ Error counting tickets: " + e.getMessage());
            return 0;
        }
    }
    
    private Ticket documentToTicket(Document doc) {
        Ticket ticket = new Ticket();
        ticket.setId(doc.getObjectId("_id").toString());
        ticket.setTicketId(doc.getString("ticketId"));
        ticket.setTitle(doc.getString("title"));
        ticket.setDescription(doc.getString("description"));
        ticket.setCategory(doc.getString("category"));
        ticket.setPriority(doc.getString("priority"));
        ticket.setStatus(doc.getString("status"));
        ticket.setRoomNumber(doc.getString("roomNumber"));
        ticket.setGuestName(doc.getString("guestName"));
        ticket.setCreatedBy(doc.getString("createdBy"));
        ticket.setCreatedAt(doc.getDate("createdAt"));
        ticket.setAssignedTo(doc.getString("assignedTo"));
        ticket.setResolvedAt(doc.getDate("resolvedAt"));
        ticket.setAdminNotes(doc.getString("adminNotes"));
        return ticket;
    }
    
    public static String formatDate(Date date) {
        if (date == null) return "N/A";
        return dateFormat.format(date);
    }
}
