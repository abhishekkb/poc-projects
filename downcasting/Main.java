
//classcastexception
class Main {
  public static class Parent {
      public String a;
  }
  public static class Child extends Parent {
      public String b;
  }
  public static class GrandChild extends Child {
      public String gc;
  }

  public static void main(String[] args) {
    Parent p = new Parent();
    Child c1 = new Child();
    Child c2 = new Child();

    GrandChild gc1 = (Grandchild) p;

    p = c1;
  }
}
