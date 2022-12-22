package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
//            for (int i = 0; i < 100; i++) {
//            Member member = new Member();
//            member.setName("member"+(i+1));
//            member.setAge(i);
//
//            em.persist(member);
//            }
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Team team2 = new Team();
            team2.setName("teamB");
            em.persist(team2);

            Member member = new Member();
            member.setName("member1");
            member.setAge(10);
            member.setTeam(team);
            em.persist(member);

            Member member2 = new Member();
            member2.setName("member2");
            member2.setAge(10);
            member2.setTeam(team2);
            em.persist(member2);

            Member member3 = new Member();
            member3.setName("member3");
            member3.setAge(10);
            member3.setTeam(team);
            em.persist(member3);


            //반환타입이 명확함
            TypedQuery<Member> typedQuery = em.createQuery("select m from Member as m", Member.class);
            //반환타입이 2가지로 다름
            Query query = em.createQuery("select m.name, m.age from Member as m");

            //1건도 없으면 빈 list 반환
            List<Member> resultList = em.createQuery("select m from Member as m", Member.class).getResultList();

//            //1개도 없으면 NoResultException
//            //1개 이상있으면 NonUniqueResultException
//            //하나만 있을때 사용해야한다, SpringDataJPA 에서는 Optional 혹은 null값을 반환함.
//            Member singleResult = em.createQuery("select m from Member as m where m.id = 1", Member.class).getSingleResult();

            TypedQuery<Member> nameQuery = em.createQuery("select m from Member as m where m.name = :name", Member.class);
            nameQuery.setParameter("name", "member1");
            Member nameQuerySingleResult = nameQuery.getSingleResult();

            System.out.println("nameQuerySingleResult = " + nameQuerySingleResult.getName());

            em.flush();
            em.clear();
            List nameAge = em.createQuery("select distinct m.name, m.age from Member as m").getResultList();

            Object o = nameAge.get(0);

            Object[] nameAgeResult = (Object[]) o;

            System.out.println("name = " + nameAgeResult[0]);
            System.out.println("age = " + nameAgeResult[1]);

            List<Object[]> nameAge2 = em.createQuery("select distinct m.name, m.age from Member as m").getResultList();
            Object[] nameAgeResult2 = nameAge2.get(0);
            System.out.println("name = " + nameAgeResult2[0]);
            System.out.println("age = " + nameAgeResult2[1]);

            List<MemberDTO> nameAge3 = em.createQuery("select distinct new jpql.MemberDTO(m.name, m.age) from Member as m").getResultList();

            MemberDTO memberDTO = nameAge3.get(0);
            System.out.println("memberDTO.getName() = " + memberDTO.getName());
            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());

            List<Member> paging = em.createQuery("SELECT m FROM Member m ORDER BY m.age desc", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("paging.size() = " + paging.size());

            for (Member member1 : paging) {
                System.out.println("member1 = " + member1);
            }

            List<Member> joinQuery = em.createQuery("SELECT m FROM Member m, Team t where m.name = t.name", Member.class).getResultList();

            List<Member> fetchJoinList = em.createQuery("SELECT m FROM Member m JOIN fetch m.team", Member.class).getResultList();
            for (Member member1 : fetchJoinList) {
                System.out.println("member1.getName() , member1.getTeam().getName() = " + member1.getName() + "," + member1.getTeam().getName());
            }

//            List<Team> fetchJoinList2 = em.createQuery("SELECT DISTINCT t FROM Team t JOIN fetch t.members", Team.class).getResultList();
//            for (Team teamResult1 : fetchJoinList2) {
//                System.out.println("team|member = " + teamResult1.getName() + "," + teamResult1.getMembers());
//                for (Member team1Member : teamResult1.getMembers()) {
//                    System.out.println("Member = " + team1Member.getName());
//                }
//            }

            List<Team> fetchJoinList3 = em.createQuery("SELECT DISTINCT t FROM Team t", Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();
            for (Team teamResult2 : fetchJoinList3) {
                System.out.println("team|member = " + teamResult2.getName() + "," + teamResult2.getMembers());
                for (Member team1Member : teamResult2.getMembers()) {
                    System.out.println("Member = " + team1Member.getName());
                }
            }

            List<Member> namedQueryList = em.createNamedQuery("Member.findByName", Member.class)
                    .setParameter("name", "member1")
                    .getResultList();
            for (Member member1 : namedQueryList) {
                System.out.println("member1 = " + member1);
            }

            int resultCount = em.createQuery("UPDATE Member m set m.age = 20")
                    .executeUpdate();

            System.out.println("result = " + resultCount);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
        emf.close();
    }
}
