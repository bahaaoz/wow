import java.util.List;

public class Parser {
    private List<String> tokens;
    private int index;
    private String currentToken;

    public Parser(List<String> tokens) {
        this.tokens = tokens;
        this.index = 0;
        this.currentToken = null;
    }

    public void parse() {
        currentToken = tokens.get(index);
        projectDeclaration();
        if (currentToken.equals(".")) {
            System.out.println("Parsing successful.");
        } else {
            System.out.println("Parsing error: Extra tokens found.");
        }
    }

    private void match(String expectedToken) {
        if (currentToken.equals(expectedToken)) {
            index++;
            if (index < tokens.size()) {
                currentToken = tokens.get(index);
            }
        } else {
            System.out.println("Parsing error: Unexpected token.");
        }
    }

    private void projectDeclaration() {
        projectDef();
        match(".");
    }

    private void projectDef() {
        projectHeading();
        declarations();
        compoundStmt();
    }

    private void projectHeading() {
        match("project");
        match("name");
        match(";");
    }

    private void declarations() {
        if (currentToken.equals("const") || currentToken.equals("var") || currentToken.equals("subroutine")) {
            constDecl();
            varDecl();
            subroutineDecl();
        }
    }

    private void constDecl() {
        if (currentToken.equals("const")) {
            match("const");
            while (!currentToken.equals("var")) {
                constItem();
                match(";");
            }
        }
    }

    private void constItem() {
        match("name");
        match("=");
        match("integer-value");
    }

    private void varDecl() {
        if (currentToken.equals("var")) {
            match("var");
            while (!currentToken.equals("subroutine")) {
                varItem();
                match(";");
            }
        }
    }

    private void varItem() {
        nameList();
        match(":");
        match("int");
    }

    private void nameList() {
        match("name");
        while (currentToken.equals(",")) {
            match(",");
            match("name");
        }
    }

    private void subroutineDecl() {
        if (currentToken.equals("subroutine")) {
            subroutineHeading();
            declarations();
            compoundStmt();
            match(";");
        }
    }

    private void subroutineHeading() {
        match("subroutine");
        match("name");
        match(";");
    }

    private void compoundStmt() {
        match("start");
        stmtList();
        match("end");
    }

    private void stmtList() {
        while (!currentToken.equals("end") && !currentToken.equals("else") && !currentToken.equals("endif")) {
            statement();
            match(";");
        }
    }

    private void statement() {
        if (currentToken.equals("name")) {
            assStmt();
        } else if (currentToken.equals("input")) {
            inoutStmt();
        } else if (currentToken.equals("if")) {
            ifStmt();
        } else if (currentToken.equals("loop")) {
            loopStmt();
        }
    }

    private void assStmt() {
        match("name");
        match(":=");
        arithExp();
    }

    private void arithExp() {
        term();
        while (currentToken.equals("+") || currentToken.equals("-")) {
            addSign();
            term();
        }
    }

    private void term() {
        factor();
        while (currentToken.equals("*") || currentToken.equals("/") || currentToken.equals("%")) {
            mulSign();
            factor();
        }
    }

    private void factor() {
        if (currentToken.equals("(")) {
            match("(");
            arithExp();
            match(")");
        } else {
            nameValue();
        }
    }

    private void nameValue() {
        if (currentToken.equals("name")) {
            match("name");
        } else if (currentToken.equals("integer-value")) {
            match("integer-value");
        } else {
            System.out.println("Parsing error: Unexpected token.");
        }
    }

    private void addSign() {
        if (currentToken.equals("+")) {
            match("+");
        } else if (currentToken.equals("-")) {
            match("-");
        } else {
            System.out.println("Parsing error: Unexpected token.");
        }
    }

    private void mulSign() {
        if (currentToken.equals("*")) {
            match("*");
        } else if (currentToken.equals("/")) {
            match("/");
        } else if (currentToken.equals("%")) {
            match("%");
        } else {
            System.out.println("Parsing error: Unexpected token.");
        }
    }

    private void inoutStmt() {
        if (currentToken.equals("input")) {
            match("input");
            match("(");
            match("name");
            match(")");
        } else if (currentToken.equals("output")) {
            match("output");
            match("(");
            nameValue();
            match(")");
        } else {
            System.out.println("Parsing error: Unexpected token.");
        }
    }

    private void ifStmt() {
        match("if");
        match("(");
        boolExp();
        match(")");
        match("then");
        statement();
        elsePart();
        match("endif");
    }

    private void elsePart() {
        if (currentToken.equals("else")) {
            match("else");
            statement();
        }
    }

    private void loopStmt() {
        match("loop");
        match("(");
        boolExp();
        match(")");
        match("do");
        statement();
    }

    private void boolExp() {
        nameValue();
        relationalOper();
        nameValue();
    }

    private void relationalOper() {
        if (currentToken.equals("=") || currentToken.equals("<>") || currentToken.equals("<") ||
                currentToken.equals("<=") || currentToken.equals(">") || currentToken.equals(">=")) {
            match(currentToken);
        } else {
            System.out.println("Parsing error: Unexpected token.");
        }
    }
}
