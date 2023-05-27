package org.example;


import org.example.cmd.CmdService;

public class Main {
    public static void main(String[] args) {
        var cmdService = new CmdService();
        cmdService.handleCmdArgs(args);
    }
}