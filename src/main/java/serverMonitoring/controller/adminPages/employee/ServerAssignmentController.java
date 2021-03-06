package serverMonitoring.controller.adminPages.employee;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import serverMonitoring.controller.adminPages.AbstractAdminController;
import serverMonitoring.logic.service.AdminService;
import serverMonitoring.logic.service.EmployeeService;
import serverMonitoring.model.ServerEntity;
import serverMonitoring.model.ftl.ServersListModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles and retrieves /admin/employee_management/serv_assignment.ftl
 * Admin must be log-in first before he can access these page.
 */
@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/employee_management/serv_assignment")
public class ServerAssignmentController extends AbstractAdminController {

    protected static Logger logger = Logger.getLogger(EmployeeManagementController.class);

    private Long idOfEmployee;

    @Autowired
    private AdminService adminService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * Retrieves /admin/employee_management/serv_assignment.ftl
     *
     * @return the name of the FreeMarker template page
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView loadPage(@PathVariable("id") Long onLoadId) {

        this.idOfEmployee = onLoadId;
        showRequestLog("serv_assignment");
        if (onLoadId != null) {

            ModelAndView model = new ModelAndView("admin/employee_management/serv_assignment");
            model.addObject("employee", employeeService.getEmployeeById(onLoadId));
            model.addObject("assignedServers", employeeService.findAllByResponsibleId(onLoadId));
            model.addObject("availableServers", employeeService.findAllByResponsibleId((long) 0));
            return model;
        } else {
            return new ModelAndView("redirect:/employee_management/employee_manager");
        }
    }

    /**
     * Handles onSubmit action
     *
     * @return the name of the FreeMarker template page
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public
    @ResponseBody
    void onSubmit(@RequestBody ServersListModel serversListModel,
                          BindingResult result,
                          SessionStatus status,
                          HttpServletRequest request,
                          HttpServletResponse response) throws ParseException, IOException {

        if (!result.hasErrors()) {
            if (serversListModel.getAssignedServers() != null) {
                for (Long id : serversListModel.getAssignedServers()) {
                    ServerEntity entity = employeeService.getServerById(id);
                    entity.setResponsible(idOfEmployee); // used global var to exclude ID bug
                    adminService.updateServer(entity);
                }
            }

            if (serversListModel.getAvailableServers() != null) {
                for (Long id : serversListModel.getAvailableServers()) {
                    ServerEntity entity2 = employeeService.getServerById(id);
                    entity2.setResponsible((long) 0);
                    adminService.updateServer(entity2);
                }
            }
            status.setComplete();
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Action on button "Cancel" pressed.
     *
     * @return redirect to employee_management page
     */
    @RequestMapping(value = "/{id}", params = "cancel", method = RequestMethod.POST)
    public ModelAndView onCancel() {
        showRequestLog("serv_assignment");
        return new ModelAndView("redirect:/employee_management/employee_manager");
    }
}
