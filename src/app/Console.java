package app;

public class Console {

    public static void printHeader() {
        System.out.println(
                "╔═══════════════════════════════════════════════════════════════╗\n" +
                        "║                                                               ║\n" +
                        "║            SISTEMI I MENAXHIMIT TË SHËNDETIT                  ║\n" +
                        "║                                                               ║\n" +
                        "╚═══════════════════════════════════════════════════════════════╝"
        );
        System.out.println("         Mirë se vini në sistemin tonë shëndetësor!\n");
    }

    public static void printMainMenu() {
        System.out.println(
                "\n════════════════════════ MENU KRYESORE ════════════════════════\n" +
                        "   P  → Pacientët\n" +
                        "   D  → Doktorët\n" +
                        "   T  → Terminet\n" +
                        "   N  → Dërgo njoftim\n" +
                        "\n" +
                        "   Q  → Dil nga aplikacioni\n" +
                        "═══════════════════════════════════════════════════════════════"
        );
    }

    public static void printPatientMenu() {
        System.out.println(
                "\n════════════════════════ PACIENTËT ═══════════════════════════\n" +
                        "   1 → Regjistro pacient të ri\n" +
                        "   2 → Shfaq të gjithë pacientët\n" +
                        "   3 → Kërko pacient me ID\n" +
                        "   4 → Modifiko të dhënat e pacientit\n" +
                        "   5 → Fshij pacient\n" +
                        "\n" +
                        "   B → Kthehu në menunë kryesore\n" +
                        "═══════════════════════════════════════════════════════════════"
        );
    }

    public static void printDoctorMenu() {
        System.out.println(
                "\n════════════════════════ DOKTORËT ════════════════════════════\n" +
                        "   1 → Regjistro doktor të ri\n" +
                        "   2 → Shfaq të gjithë doktorët\n" +
                        "   3 → Kërko doktor me ID\n" +
                        "   4 → Modifiko të dhënat e doktorit\n" +
                        "   5 → Fshij doktor\n" +
                        "\n" +
                        "   B → Kthehu mbrapa\n" +
                        "═══════════════════════════════════════════════════════════════"
        );
    }

    public static void printAppointmentMenu() {
        System.out.println(
                "\n════════════════════════ TERMINET ════════════════════════════\n" +
                        "   1 → Planifiko termin të ri\n" +
                        "   2 → Shfaq të gjitha terminet\n" +
                        "   3 → Shfaq terminet për një pacient\n" +
                        "   4 → Shfaq terminet sipas statusit\n" +
                        "   5 → Përfundo termin\n" +
                        "   6 → Anulo termin\n" +
                        "   7 → Fshij termin\n" +
                        "\n" +
                        "   B → Kthehu mbrapa\n" +
                        "═══════════════════════════════════════════════════════════════"
        );
    }

    public static void printNotiftMenu() {
        System.out.println(
                "\n════════════════════════ NJOFTIMET ═══════════════════════════\n" +
                        "   1 → Dërgo Email\n" +
                        "   2 → Dërgo SMS\n" +
                        "\n" +
                        "   B → Kthehu mbrapa\n" +
                        "═══════════════════════════════════════════════════════════════"
        );
    }

    public static void section(String title) {
        System.out.println(
                "\n════════════════ " + title.toUpperCase() + " ════════════════"
        );
    }

    public static void success(String msg) {
        System.out.printf(
                "\n╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n" +
                        "║                                                                                                                ║\n" +
                        "║   SUKSES                                                                                                       ║\n" +
                        "║                                                                                                                ║\n" +
                        "║   %-108s ║\n" +
                        "║                                                                                                                ║\n" +
                        "╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n",
                msg
        );
    }

    public static void error(String msg) {
        System.out.printf(
                "\n╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗\n" +
                        "║                                                                                                                ║\n" +
                        "║   GABIM                                                                                                        ║\n" +
                        "║                                                                                                                ║\n" +
                        "║   %-108s ║\n" +
                        "║                                                                                                                ║\n" +
                        "╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n",
                msg
        );
    }
}