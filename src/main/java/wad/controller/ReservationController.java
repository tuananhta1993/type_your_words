package wad.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wad.domain.Account;
import wad.domain.Reservation;
import wad.repository.AccountRepository;
import wad.repository.ReservationRepository;
import wad.service.SendHTMLEmail;
import wad.service.SendSMS;

@Controller
public class ReservationController {
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private ReservationRepository reservationRepository;
    private Object formatter;
    
    @Autowired
    private SendHTMLEmail sendHTMLEmail;
    
    @Autowired
    private SendSMS sendSMS;
    
    @RequestMapping(value = "/reservations", method = RequestMethod.GET)
    public String view(Model model) {
        List<Reservation> reservations = new ArrayList();
        reservations=reservationRepository.findAll();
        if (reservations==null) {
            reservations = new ArrayList();
        }
        
        model.addAttribute("reservations", reservations);
        
        return "reservations";
    }
    
    @RequestMapping(value = "/calendar", method = RequestMethod.GET)
    public String calendar(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
                        
        model.addAttribute("username", username);
        Boolean isAuthenticated;
        isAuthenticated = (!("anonymousUser".equals(username)));
        model.addAttribute("isAuthenticated", isAuthenticated);
        
        return "calendar";
    }
    
    @RequestMapping(value = "/reservations", method = RequestMethod.POST)
    public String add(@RequestParam String start_date, String end_date, String start_time, String end_time, String subject, String isPublicText, HttpServletResponse response) throws ParseException, IOException {
        Reservation res = new Reservation();
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));

        Date startDate = (Date)format.parse(start_date+" "+start_time); 
        Date endDate = (Date)format.parse(end_date+" "+end_time);
        Boolean isOverlapped = false;
        
        if (startDate.compareTo(endDate)<=0) {
            List<Reservation> reservations = reservationRepository.findAll();
            if (reservations!=null) {
                for(Reservation reservation : reservations)
                {
                    if(isOverlapped==false)
                    {
                        // fully overlap
                        if ((startDate.compareTo(reservation.getReservationFrom())<=0)&(endDate.compareTo(reservation.getReservationTo())>=0)) {
                            isOverlapped=true;
                        }
                        
                        // overlap start
                        if ((startDate.compareTo(reservation.getReservationFrom())>=0)&(startDate.compareTo(reservation.getReservationTo())<=0)) {
                            isOverlapped=true;
                        }
                        
                        // overlap end
                        if ((endDate.compareTo(reservation.getReservationFrom())>=0)&(endDate.compareTo(reservation.getReservationTo())<=0)) {
                            isOverlapped=true;
                        }
                    }
                }
            }
            
            if (isOverlapped==false) {
                res.setReservationFrom(new Timestamp(startDate.getTime()));
                res.setReservationTo(new Timestamp(endDate.getTime()));

                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String username = auth.getName();

                res.setAccount(accountRepository.findByUsername(username));
                res.setUsername(username);
                
                System.out.println(username);
                
                System.out.print(isPublicText);
                if ("yes".equals(isPublicText)) 
                    res.setIsPublic(true);
                else
                    res.setIsPublic(false);
                
                res.setSubject(subject);
                             
                reservationRepository.save(res);
                
                String email = res.getAccount().getEmail();
                String phone = res.getAccount().getPhone();
                String phone_msg = "";
                String email_body = "";
                
                email_body = "<h1>*(Demo only)</h1>" + "<br><hr><br>";
                email_body += "Thank you for your reservation. Here is your basic info:" + "<br><br>";
                email_body += "From: " + format.format(startDate) + "<br>";
                email_body += "To: " + format.format(endDate) + "<br>";
                email_body += "Purpose: " + res.getSubject() + "<br>";
                
                phone_msg = "*(Demo only)" + "\n" + "\n";
                phone_msg += "Thank you for your reservation. Here is your basic info:" + "\n";
                phone_msg += "From: " + format.format(startDate) + "\n";
                phone_msg += "To: " + format.format(endDate) + "\n";
                phone_msg += "Purpose: " + res.getSubject() + "\n";
                
                String email_body_final = email_body;
                String phone_msg_final = phone_msg;
                
                // Send email notification
                new Thread(() -> sendHTMLEmail.send_mail("tatuananh.fi@gmail.com",email,"(Demo) Reservation confirmation.",email_body_final)).start();
                new Thread(() -> {
                    try {
                        sendSMS.send(phone,phone_msg_final);
                    } catch (IOException ex) {
                        Logger.getLogger(ReservationController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }).start();
                
            }
        }

        PrintWriter out = response.getWriter();
        
        if (isOverlapped) {
            out.println("Overlapped");
        }else
        {
            out.println("OK");
        }
        
        return null;
    }
    
    @RequestMapping(value = "/reservations", method = RequestMethod.DELETE)
    public String delete(@RequestParam Long id, HttpServletResponse response) throws ParseException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        String error_msg = "";
        System.out.println(id.toString());
                
        Reservation res = reservationRepository.findOne(id);
        String reservation_username = res.getAccount().getUsername();
        
        System.out.println(reservation_username);
        System.out.println(username);
        
        if (username.equals(reservation_username)) {
            reservationRepository.delete(id);
        }else
        {
            error_msg+="This event doesn't belong to your account. Please select your reservation only.";
        }
        
        PrintWriter out = response.getWriter();
        
        if (error_msg.length()>0) {
            out.println(error_msg);
        }else
        {
            out.println("OK");
        }
        
        return null;
    }
}