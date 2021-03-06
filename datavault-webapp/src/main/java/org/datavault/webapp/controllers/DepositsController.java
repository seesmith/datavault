package org.datavault.webapp.controllers;


import org.datavault.common.model.Deposit;
import org.datavault.common.model.Withdrawal;
import org.datavault.webapp.services.RestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * User: Robin Taylor
 * Date: 19/03/2015
 * Time: 13:32
 */

@Controller
public class DepositsController {

    private RestService restService;

    public void setRestService(RestService restService) {
        this.restService = restService;
    }

    // Return an 'create new deposit' page
    @RequestMapping(value = "/vaults/{vaultid}/deposits/create", method = RequestMethod.GET)
    public String createDeposit(@ModelAttribute Deposit deposit, ModelMap model, @PathVariable("vaultid") String vaultID) {
        // pass the view an empty Deposit since the form expects it
        model.addAttribute("deposit", new Deposit());
        model.addAttribute("vaultID", vaultID);
        return "/deposits/create";
    }

    // Process the completed 'create new deposit' page
    @RequestMapping(value = "/vaults/{vaultid}/deposits/create", method = RequestMethod.POST)
    public String addDeposit(@ModelAttribute Deposit deposit, ModelMap model, @PathVariable("vaultid") String vaultID) {
        Deposit newDeposit = restService.addDeposit(vaultID, deposit);
        String depositUrl = "/vaults/" + vaultID + "/deposits/" + newDeposit.getID() + "/";
        return "redirect:" + depositUrl;
    }

    // View properties of a single deposit
    @RequestMapping(value = "/vaults/{vaultid}/deposits/{depositid}", method = RequestMethod.GET)
    public String getDeposit(ModelMap model, @PathVariable("vaultid") String vaultID, @PathVariable("depositid") String depositID) {
        model.addAttribute("vault", restService.getVault(vaultID));
        model.addAttribute("deposit", restService.getDeposit(vaultID, depositID));
        model.addAttribute("manifest", restService.getDepositManifest(vaultID, depositID));
        model.addAttribute("events", restService.getDepositEvents(vaultID, depositID));
        return "deposits/deposit";
    }
    
    // Return an 'withdraw deposit' page
    @RequestMapping(value = "/vaults/{vaultid}/deposits/{depositid}/withdraw", method = RequestMethod.GET)
    public String withdrawDeposit(@ModelAttribute Withdrawal withdrawal, ModelMap model, @PathVariable("vaultid") String vaultID, @PathVariable("depositid") String depositID) {
        model.addAttribute("withdrawal", new Withdrawal());
        model.addAttribute("deposit", restService.getDeposit(vaultID, depositID));
        return "deposits/withdraw";
    }
    
    // Process the completed 'withdraw deposit' page
    @RequestMapping(value = "/vaults/{vaultid}/deposits/{depositid}/withdraw", method = RequestMethod.POST)
    public String processWithdrawal(@ModelAttribute Withdrawal withdrawal, ModelMap model, @PathVariable("vaultid") String vaultID, @PathVariable("depositid") String depositID) {
        
        Boolean result = restService.withdrawDeposit(vaultID, depositID, withdrawal);
        
        String depositUrl = "/vaults/" + vaultID + "/deposits/" + depositID + "/";
        return "redirect:" + depositUrl;
    }

    
    /*
    @RequestMapping(value = "/deposits", method = RequestMethod.GET)
    public String createDeposit(ModelMap model) {
        model.addAttribute("files", restService.getFilesListing());
        return "files";
    }


    @RequestMapping(value = "/deposits", method = RequestMethod.GET)
    public String getFilesListing(ModelMap model) {
        model.addAttribute("files", restService.getFilesListing());
        return "files";
    }
*/

}
