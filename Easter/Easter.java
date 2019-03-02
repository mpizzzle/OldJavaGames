public class Easter
{
    private static String USER1 = "Michael";
    private static String USER2 = "Stephen";
    private static String CONSTANT1 = "DFCHQ";
    private static String CONSTANT2 ="MAINCOREOFWEAS";
    /**
     * Main entry point for Easter class.
     *
     * @param parameters is an array of string parameters of any size
     * 'void' means it doesn't return a result value, like a function would
     * don't worry about the 'static' word - it's mandatory for standalone programs
     */
    public static void main(String[] parameters)
    {
        int i = 0;
        String valuea = "";
        String valueb = "";
        String text = "Go and look here: ";
        boolean variable1 = true, variable2 = false, variable3 = true;
        if (parameters.length < 1)
        {
            System.out.println("Please put your name as the parameter to this program and try running it again");
        }
        else if (USER1.equalsIgnoreCase(parameters[0]) || USER2.equalsIgnoreCase(parameters[0]))   // || means 'OR'
        {
            System.out.println("Welcome, " + parameters[0]);  // print out to the screen element 0 (the first element) in the array
            if (USER1.equalsIgnoreCase(parameters[0]))        // case-independent comparison, i.e. 'M' matches 'm'
            {
                valuea = CONSTANT1;
                if (variable1 || variable2)
                {
                    valuea = valuea + 'E';
                }
                if (variable2 && variable3)       // && means 'AND'
                {
                    valuea = valuea + 'E';
                }
                valueb = doSomething(valuea);
                i = 0;
                while (i < valueb.length())
                {
                    // add 1 to character at position i
                    char c = (char) (valueb.charAt(i) + 1);  // in order to add 1 to a character it has to be of type 'char' rather than String - don't worry about it!
                    text = text + c; // charAt(i) gives you the character at position(i). NB first character is at position 0
                    i = i + 1;
                }
            }
            else if (USER2.equalsIgnoreCase(parameters[0]))
            {
                valueb = CONSTANT2;
                // charAt(i) gives you the character at position(i). NB first character is at position 0
                // && means 'AND'; 0x in front of a number means that number is hexadecimal
                if (0x11 > 15 && USER1.charAt(0) > valueb.charAt(0))
                {
                     valueb = valueb + "AV";
                }
                else
                {
                     valueb = valueb + "VA";
                }
                if (Math.sqrt(121) > 8)   // sqrt means 'square root'
                {
                    valueb = valueb + 'E';
                }
                else
                {
                    valueb = valueb + 'A';
                }
                if (variable1 && (variable2 || variable3))       // && means 'AND'; || means 'OR'
                {
                    valueb = valueb + 'R';
                }
                else
                {
                    valueb = valueb + 'S';
                }
                i = 0;
                while (i < valueb.length())
                {
                    if (i % 2 == 0)     // % is the modulo or 'remainder' function (i.e. remainder from dividing) and != means not equal to
                    {
                        text = text + (valueb.charAt(i)); // charAt(i) gives you the character at position(i). NB first character is at position 0
                    }
                    i = i + 1;
                }
            }
            System.out.println(text);
        }
        else
        {
            System.out.println("Sorry, " + parameters[0] + " gets nothing");
        }

    }

    private static String doSomething(String input)
    {
        String result = "";
        if (input.length() > 1)
        {
            result = doSomething(input.substring(1, input.length())); // do something with the substring minus the first character (substring from element 1 to the end)
        }
        result = result + input.charAt(0); // charAt(0) means the first character 
        return result;
    }

}
