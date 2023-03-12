public class SysTick implements Cortex_M0_SysTick_Interface
{
 public int csr;

    public int rvr;

    public int cvr;

    @Override
    public void setRVR(int value) {
        int value_ref = (int) Math.pow(2,24);
        if (value >= (Math.pow(2,24))) {
            rvr = value - value_ref;

        } else if (value < 0) {
            rvr = value_ref - Math.abs(value);
            //System.out.println(Math.abs(value));
        } else {rvr = value;}
    }
    @Override
    public void setCVR(int value) {

        value=0;
        cvr=value;
        resetFlag();

    }

    @Override
    public void setCSR(int value) {
        csr=value;
    }

    @Override
    public int getCVR() {

        return cvr;
    }

    @Override
    public int getRVR() {
        return rvr;
    }

    @Override
    public int getCSR() {

        resetFlag();
        return csr;



    }

    @Override //
    public void reset(int value) {

    }

    @Override
    public void setEnable() {
    csr=csr|0x1;
    }


    @Override
    public void setDisable() {
     csr=csr&0xFFFFFFFE;
    }

    @Override
    public void setSourceExternal() {
        csr=(csr|0x4);
    }

    @Override
    public void setSourceInternal() {
        csr=csr&0xFFFFFFFB;
    }

    @Override
    public void setInterruptEnable() {
    csr=csr|0x2;

    }

    @Override
    public void setInterruptDisable() {
    csr=csr&0xFFFFFFFD;
    }

    @Override
    public boolean getEnabled() {
     int val=csr&0x1;
        return val == 0x1;


    }


    @Override //zeruje countflag
    public boolean getInterrupt() {
     int val=csr&0x2;
     resetFlag();
     return val == 0x2;

    }

    @Override //zeruje countflag
    public boolean getSource() {
     int val=csr&0x4;
     resetFlag();
        return val == 0x4;

    }

    @Override //zeruje countflag
    public boolean getCountFlag() {
        int val = csr & 0x10000;
        resetFlag();
        return (val == 0x10000);
    }


    @Override

    public void tickInternal() {
        if(sourceSelInter())
        {
        ticker();
        }
    }

    @Override
    public void tickExternal() {
        if(sourceSelExter())
        {

        ticker();
        }
    }

    @Override //
    public boolean isCountFlag() {
        int val = csr & 0x10000;
        return (val == 0x10000);

    }

    @Override //
    public boolean isEnableFlag() {
        int val=csr&0x1;
        return val == 0x1;
    }

    @Override //
    public boolean isInterruptFlag() {
        int val=csr&0x2;
        return val == 0x2;
    }

    @Override //
    public boolean isInterrupt() {

        //return (csr & 2) == 2 && (csr & 0x10000) == 0x10000;
        return isInterruptFlag();
    }
public void setFlag()
{
    csr=csr|0x10000;

}
    public void resetFlag()
    {
        csr=csr&0xFFFEFFFF;
    }

public boolean sourceSelInter()
{

    int val=csr&0x4;
    return val != 0x4;
}
    public boolean sourceSelExter()
    {
        int val=csr&0x4;
        return val == 0x4;
    }
public void ticker()
{

    if(rvr==0) {
        cvr = rvr;    //stinky code
        setDisable();
    }
    if (getEnabled())
    {

        if(cvr<=0) {
            cvr = rvr;
        }
        else
            cvr--;
        System.out.println("tickCont");

        if (cvr == 0) {
            setFlag();

        }
    }
}

}

 /*   reset() - zerowanie status register, zegar ustawiony na zewnętrzny, kasuje flage przejścia przez zero, nie rusza cvr i rvr
        disable() - nie reagowanie na tickInternal/External
        source() - ustaw taktowanie
        interrupt() - przełącznik przerwań


        getInterrupt() - czy są włączone przerwania
        getSource() - które źródło jest używane
        getCounterFlag()

        is___ - sprawdzenie wartości bez zmiany wartości rejestru
        isCountFlag() - czy jest zgłoszona flaga
        isInterrupt() - czy jest przerwanie
        isInterruptFlag() - czy jest włączone przerwanie


        Co zrobić:
        - ogarniczyć int do 24bit (może sprawdzać czy może wpisać większą liczbe)
        - counter = 0 => coutnflag = 1
        - wpisanie czegokolwiek do cvr => następuje zerowanie (zabezpieczenie przed za długim wykonywaniem procesu)
        - wszystko ma być w funkcji tickInternal/External

        tickInternal - obniża wartość cvr

        Czemu get zerują countFlag i inne bity też? - Bo każdy odczyt rejestry zeruje countFlag i tyle xd

        Stworzyć klase rejestr - Klasa SysTick ma być jedynym interfejsem dla użytkownika, może ona korzsytać z instancji klasy rejestr i ich
        metod (set i get).

        isCountFlag  - czy przerwanie zostało wysłane (tylko do testów)

        Generator liczb - ostatni bit temperatury procesora (szum)

        używać get(rejestr) w get(systick) 	*/

