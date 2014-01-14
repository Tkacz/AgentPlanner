AgentPlanner
----------------------------------------
----------------------------------------

Aktualna wersja nieco różni się od algorytmu przedstawionego w pracy mgr/artykule z WASA. Najważniejsza zmiana dotyczy sprawdzania kolizji studentów. Algorytm podczas etapu negocjacji nie sprawdza kolizji dla każdej propozycji równolegle, tylko robi to iteracyjnie, sortując wcześniej propozycje po ich ocenach. Taka zmiana spowalnia nieco działanie algorytmu, ale całkowicie eliminuje kolizje studentów (chyba, że zechcemy wprowadzić próg tolerancji kolizji dla studentów, np., że zgadzamy się na 10% kolizji).

Druga zmiana dotyczy czynnika losowego w funkcji oceny miejsca w planie, a właściwie jego eliminacji. Doszliśmy do wniosku, że agent reprezentuje człowieka, który wie czego chce, więc jakakolwiek losowość jest nieadekwatna. Losowy czynnik jest potrzebny w momencie, gdy podczas negocjacji jest kilka propozycji o tej samej ocenie i potrzebny jest konkurs, wówczas zwycięzca jest losowany.
