/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gorgonengine;

/**
 *
 * @author Andreas
 */

import java.lang.Math.*;
import java.util.Vector;

public class LinAlg {
    
    public final static double EPSILON = 0.00000001;
    public static final double ORENNAYAR_STANDARD_DEVIATION = Math.PI/4;
    public static final double PHOTON_SEARCH_RADIUS = 0.5;
    public static final double STANDARD_FLUX = 1;
    public static final int STANDARD_PHOTON_EMITTANCE = 1000000;
    public static Node<PhotonContainer> octreeRoot;
    public static int missingPhotons;
    public static int hitSphere;
    
    public static void displayWholeOctree()
    {
        int length = 0;
        length = octreeRoot.traverse(octreeRoot, length);
        System.out.println("Whole length: " + length);
    }
    
    public static int getPhotonSubsetFromOctree(int index, Node<PhotonContainer> it)
    {
        int result = 0;
        if(it.dataEquals(octreeRoot.returnData()))
        {
            result += getPhotonSubsetFromOctree(index, it.returnChild(index));
        }
        else if(it.checkIfParent())
        {
            for(int idn = 0; idn < it.returnChildrenAmount(); ++idn)
            {
                result += getPhotonSubsetFromOctree(index, it.returnChild(idn));
            }
        }
        else
        {
            result = it.returnData().getContainerSize();
        }
        return result;
    }
    
    public static boolean checkInside(Vertex pos, Vertex max, Vertex min)
    {
        boolean result = true;
        
        if(pos.x <= max.x  && 
                    pos.y <= max.y && 
                    pos.z <= max.z)
                result = true;
            else
                result = false;
            if(pos.x >= min.x && 
                    pos.y >= min.y && 
                    pos.z >= min.z && result)
                result = true;
            else
                result = false;
        
        return result;
    }
    
    public static void emitPhotons()
    {
        int percentage1 = 0, percentage2 = 1;
        Vector<Object> lightsources = new Vector<Object>();
        for(int ido = 0; ido < Scene.objects.length; ++ido)
        {
            if(Scene.objects[ido].isLightsource())
                lightsources.add(Scene.objects[ido]);
        }
        for(int idl = 0; idl < lightsources.size(); ++idl)
        {
            if(!lightsources.get(idl).isSphere())
            {
                for(int idt = 0; idt < lightsources.get(idl).getSize(); ++idt)
                {
                    Triangle curTriangle = lightsources.get(idl).returnTriangleByIndex(idt);
                    for(int ide = 0; ide < STANDARD_PHOTON_EMITTANCE/lightsources.get(idl).getSize(); ++ide)
                    {
                        if(ide > (double)percentage2*STANDARD_PHOTON_EMITTANCE/lightsources.get(idl).getSize()/100)
                        {
                            ++percentage1;
                            System.out.println("Photon emittance at " + percentage1 + "%!");
                            ++percentage2;
                        }
                        Vertex emittancePoint = randomPointOnTriangle(curTriangle);
                        Direction randomDir = getRandomDirection(false, curTriangle.normal);
                        Vertex setEnd = VektorAddition(emittancePoint, VektorMultiplikation(dirToVertex(randomDir), 100));
                        Ray lightRay = new Ray(emittancePoint, setEnd, ColorDbl.BLACK, -5, Ray.RAY_LIGHT);
                        Scene.lightRayIntersection(lightRay);
                    }
                }
            }
            else
            {
                Sphere lightSphere = (Sphere)lightsources.get(idl);
                for(int ide = 0; ide < STANDARD_PHOTON_EMITTANCE; ++ide)
                {
                    Vertex emittancePoint = lightSphere.getRandomPointOnSphere();
                    Direction normal = new Direction(VektorSubtraktion(emittancePoint, lightSphere.center));
                    normal = normalize(normal);
                    Direction randomDir = getRandomDirection(true, normal);
                    Vertex setEnd = VektorAddition(emittancePoint, VektorMultiplikation(dirToVertex(randomDir), 100));
                    Ray lightRay = new Ray(emittancePoint, setEnd, ColorDbl.BLACK, -5, Ray.RAY_LIGHT);
                    Scene.lightRayIntersection(lightRay);
                }
            }
        }
        System.out.println("Finished emitting photons!!!");
    }
    
    public static Direction getRandomDirection(boolean isSphere, Direction normal)
    {
        Direction result = new Direction();
        HemisCoords newDir = new HemisCoords();
        double maxThetaAngle = 0;
        if(!isSphere)
        {
            maxThetaAngle = Math.PI/2;
        }
        else
        {
            maxThetaAngle = Math.PI/2;
        }
        newDir = cartToHemis(normal);
        double newTheta = UNIPDF2theta();
        do
        {
            newTheta = UNIPDF2theta();
        }while(newTheta > maxThetaAngle && newTheta < 0);
        newTheta += Math.PI;
        double newPhi = UNIPDF2phi();
        do
        {
            newPhi = UNIPDF2phi();
        }while(newPhi < 0 && newPhi >= 2*Math.PI);
        newDir.r = 1;
        newDir.theta = newTheta;
        newDir.phi = newPhi;
//        System.out.println("Theta: "+(newDir.theta/Math.PI));
        result = vertexToDir(hemisToCart(newDir));
//        System.out.println("Vector: "+result.toString());
        return result;
    }
    
    public static double UNIPDF()
    {
        double randVal = Math.random()*2;
        return 0.75*(1-Math.pow(randVal-1,2));
    }
    
    public static double UNIPDF2phi()
    {
        return 2*Math.PI*Math.random();
    }
    
    public static double UNIPDF2theta()
    {
        return Math.acos(Math.sqrt(Math.random()));
    }
    
    public static void createOctree(Node<PhotonContainer> root)
    {
        Vertex max = root.returnData().getMaxPos(), min = root.returnData().getMinPos();
        Vertex mid = new Vertex((min.x + max.x)/2, (min.y + max.y)/2, (min.z + max.z)/2);
        double xMod = max.x - mid.x, yMod = max.y - mid.y, zMod = max.z - mid.z;
        
        for(int idnr = 0; idnr < 8; ++idnr)
        {
            Vertex newMax = Vertex.DUMMY, newMin = Vertex.DUMMY;
            switch(idnr)
            {
                case 0:
                    newMax = new Vertex(mid.x + xMod, mid.y + yMod, mid.z + zMod);
                    newMin = mid;
                    break;
                case 1:
                    newMax = new Vertex(mid.x + xMod, mid.y, mid.z + zMod);
                    newMin = new Vertex(mid.x, mid.y - yMod, mid.z);
                    break;
                case 2:
                    newMax = new Vertex(mid.x + xMod, mid.y + yMod, mid.z);
                    newMin = new Vertex(mid.x, mid.y, mid.z - zMod);
                    break;
                case 3:
                    newMax = new Vertex(mid.x + xMod, mid.y, mid.z);
                    newMin = new Vertex(mid.x, mid.y - yMod, mid.z - zMod);
                    break;
                case 4:
                    newMax = new Vertex(mid.x, mid.y + yMod, mid.z + zMod);
                    newMin = new Vertex(mid.x -xMod, mid.y, mid.z);
                    break;
                case 5:
                    newMax = new Vertex(mid.x, mid.y, mid.z + zMod);
                    newMin = new Vertex(mid.x - xMod, mid.y - yMod, mid.z);
                    break;
                case 6:
                    newMax = new Vertex(mid.x, mid.y + yMod, mid.z);
                    newMin = new Vertex(mid.x - xMod, mid.y, mid.z - zMod);
                    break;
                case 7:
                    newMax = mid;
                    newMin = new Vertex(mid.x - xMod, mid.y - yMod, mid.z - zMod);
                    break;
            }
            double[] newDiameters = new double[]{returnLength(new Vertex(newMax.x - newMin.x, 0, 0)), 
                                                 returnLength(new Vertex(0, newMax.y - newMin.y, 0)), 
                                                 returnLength(new Vertex(0, 0, newMax.z - newMin.z))};
            for(int idd = 0; idd < 3; ++idd)
            {
//                System.out.println("Displaying diameters: " + newDiameters[idd]);
            }
            Node<PhotonContainer> it = new Node<PhotonContainer>(new PhotonContainer(newMax, newMin, newDiameters));
            
            root.addChild(it);
        }
    }
    
    public static void addPhotonToTree(Vertex pos, double flux, Direction dir, int type, Node<PhotonContainer> cur)
    {
        boolean inAny = false;
        for(int idb = 0; idb < octreeRoot.returnChildrenAmount(); ++idb)
        {
            boolean in = true;
//            System.out.println("Position: " + pos);
//            System.out.println("Maxpos: " + cur.returnChild(idb).returnData().getMaxPos());
//            System.out.println("Minpos: " + cur.returnChild(idb).returnData().getMinPos());
            if(pos.x <= cur.returnChild(idb).returnData().getMaxPos().x && 
                    pos.y <= cur.returnChild(idb).returnData().getMaxPos().y && 
                    pos.z <= cur.returnChild(idb).returnData().getMaxPos().z)
                in = true;
            else
                in = false;
            if(pos.x >= cur.returnChild(idb).returnData().getMinPos().x && 
                    pos.y >= cur.returnChild(idb).returnData().getMinPos().y && 
                    pos.z >= cur.returnChild(idb).returnData().getMinPos().z && in)
                in = true;
            else
                in = false;
            if(in)
            {
                inAny = true;
//                System.out.println("We are in!");
                boolean checkDiameters = false;
                double[] curDiameters = cur.returnChild(idb).returnData().getDiameters();
                for(int idd = 0; idd < 3; ++idd)
                {
                    if(curDiameters[idd] < PHOTON_SEARCH_RADIUS)
                    {
                        checkDiameters = true;
                        break;
                    }
                }
                if(!checkDiameters)
                {
                    if(!cur.returnChild(idb).checkIfParent())
                    {
//                        System.out.println("Adding more children!");
                        createOctree(cur.returnChild(idb));
                    }
                    addPhotonToTree(pos, flux, dir, type, cur.returnChild(idb));
                }
                else
                {
                    
                    cur.returnChild(idb).returnData().addPhoton(new Photon(pos, flux, dir, type));
                }
                break;
            }
        }
        if(!inAny)
        {
            ++missingPhotons;
//            System.out.println("Not in any.");
//            System.out.println("Position: " + pos);
//            System.out.println("maxPos: " + cur.returnData().getMaxPos());
//            System.out.println("minPos: " + cur.returnData().getMinPos());
        }
            
    }
    //Här är problemet, du tar inte med den node som positionen ligger i!
    public static Vector<Photon> getPhotons(Vertex pos, Node<PhotonContainer> cur)
    {
        Vector<Photon> result = new Vector<Photon>();
        Vector<Node<PhotonContainer>> reachNodes = new Vector<Node<PhotonContainer>>();
        Vertex superMax = cur.returnData().getMaxPos();
        Vertex superMin = cur.returnData().getMinPos();
        Vertex superMid = new Vertex((superMin.x + superMax.x)/2, (superMin.y + superMax.y)/2, (superMin.z + superMax.z)/2);
        for(int idb = 0; idb < 8; ++idb)
        {
            
            

            double xmod = pos.x - superMid.x, ymod = pos.y - superMid.y, zmod = pos.z - superMid.z;
            
            Vertex[] closestPoints = new Vertex[7];
            switch(idb)
            {
                case 0:
                    closestPoints[0] = new Vertex(superMid.x + xmod, superMid.y, superMid.z + zmod);
                    closestPoints[1] = new Vertex(superMid.x + xmod, superMid.y + ymod, superMid.z);
                    closestPoints[2] = new Vertex(superMid.x + xmod, superMid.y, superMid.z);
                    closestPoints[3] = new Vertex(superMid.x, superMid.y + ymod, superMid.z + zmod);
                    closestPoints[4] = new Vertex(superMid.x, superMid.y, superMid.z + zmod);
                    closestPoints[5] = new Vertex(superMid.x, superMid.y + ymod, superMid.z);
                    closestPoints[6] = superMid;
                    break;
                case 1:
                    closestPoints[0] = new Vertex(superMid.x + xmod, superMid.y, superMid.z + zmod);
                    closestPoints[1] = new Vertex(superMid.x + xmod, superMid.y, superMid.z);
                    closestPoints[2] = new Vertex(superMid.x + xmod, superMid.y - ymod, superMid.z);
                    closestPoints[3] = new Vertex(superMid.x, superMid.y, superMid.z + zmod);
                    closestPoints[4] = new Vertex(superMid.x, superMid.y - ymod, superMid.z + zmod);
                    closestPoints[5] = superMid;
                    closestPoints[6] = new Vertex(superMid.x, superMid.y - ymod, superMid.z);
                    break;
                case 2:
                    closestPoints[0] = new Vertex(superMid.x + xmod, superMid.y + ymod, superMid.z);
                    closestPoints[1] = new Vertex(superMid.x + xmod, superMid.y, superMid.z);
                    closestPoints[2] = new Vertex(superMid.x + xmod, superMid.y, superMid.z - zmod);
                    closestPoints[3] = new Vertex(superMid.x, superMid.y + ymod, superMid.z);
                    closestPoints[4] = superMid;
                    closestPoints[5] = new Vertex(superMid.x, superMid.y + ymod, superMid.z - zmod);
                    closestPoints[6] = new Vertex(superMid.x, superMid.y, superMid.z - zmod);
                    break;
                case 3:
                    closestPoints[0] = new Vertex(superMid.x + xmod, superMid.y, superMid.z);
                    closestPoints[1] = new Vertex(superMid.x + xmod, superMid.y - ymod, superMid.z);
                    closestPoints[2] = new Vertex(superMid.x + xmod, superMid.y, superMid.z - zmod);
                    closestPoints[3] = superMid;
                    closestPoints[4] = new Vertex(superMid.x, superMid.y - ymod, superMid.z);
                    closestPoints[5] = new Vertex(superMid.x, superMid.y, superMid.z - zmod);
                    closestPoints[6] = new Vertex(superMid.x, superMid.y - ymod, superMid.z - zmod);
                    break;
                case 4:
                    closestPoints[0] = new Vertex(superMid.x, superMid.y + ymod, superMid.z + zmod);
                    closestPoints[1] = new Vertex(superMid.x, superMid.y, superMid.z);
                    closestPoints[2] = new Vertex(superMid.x, superMid.y + ymod, superMid.z);
                    closestPoints[3] = superMid;
                    closestPoints[4] = new Vertex(superMid.x - xmod, superMid.y, superMid.z + zmod);
                    closestPoints[5] = new Vertex(superMid.x - xmod, superMid.y + ymod, superMid.z);
                    closestPoints[6] = new Vertex(superMid.x - xmod, superMid.y + ymod, superMid.z + zmod);
                    break;
                case 5:
                    closestPoints[0] = new Vertex(superMid.x, superMid.y, superMid.z + zmod);
                    closestPoints[1] = new Vertex(superMid.x, superMid.y - ymod, superMid.z + zmod);
                    closestPoints[2] = superMid;
                    closestPoints[3] = new Vertex(superMid.x, superMid.y - ymod, superMid.z);
                    closestPoints[4] = new Vertex(superMid.x - xmod, superMid.y, superMid.z + zmod);
                    closestPoints[5] = new Vertex(superMid.x - xmod, superMid.y, superMid.z);
                    closestPoints[6] = new Vertex(superMid.x - xmod, superMid.y - ymod, superMid.z);
                    break;
                case 6:
                    closestPoints[0] = new Vertex(superMid.x, superMid.y + ymod, superMid.z);
                    closestPoints[1] = superMid;
                    closestPoints[2] = new Vertex(superMid.x, superMid.y + ymod, superMid.z - zmod);
                    closestPoints[3] = new Vertex(superMid.x, superMid.y, superMid.z - zmod);
                    closestPoints[4] = new Vertex(superMid.x - xmod, superMid.y + ymod, superMid.z);
                    closestPoints[5] = new Vertex(superMid.x - xmod, superMid.y, superMid.z);
                    closestPoints[6] = new Vertex(superMid.x - xmod, superMid.y + ymod, superMid.z - zmod);
                    break;
                case 7:
                    closestPoints[0] = superMid;
                    closestPoints[1] = new Vertex(superMid.x, superMid.y - ymod, superMid.z);
                    closestPoints[2] = new Vertex(superMid.x, superMid.y, superMid.z - zmod);
                    closestPoints[3] = new Vertex(superMid.x, superMid.y - ymod, superMid.z - zmod);
                    closestPoints[4] = new Vertex(superMid.x - xmod, superMid.y, superMid.z);
                    closestPoints[5] = new Vertex(superMid.x - xmod, superMid.y - ymod, superMid.z);
                    closestPoints[6] = new Vertex(superMid.x - xmod, superMid.y, superMid.z - zmod);
                    break;
                default:
                    System.out.println("Something went wrong with closestPoints!: " + idb);
                    break;
            }
            Vertex localMax = cur.returnChild(idb).returnData().getMaxPos();
            Vertex localMin = cur.returnChild(idb).returnData().getMinPos();
            if(pos.x <= localMax.x && pos.y <= localMax.y && pos.z <= localMax.z && 
                    pos.x >= localMin.x && pos.y >= localMin.y && pos.z >= localMin.z)
            {
                reachNodes.add(cur.returnChild(idb));
            }
            int offset = 0;
            boolean offsetplus = true;
            for(int idp = 0; idp < 7; ++idp)
            {
                double curDist = returnLength(VektorSubtraktion(closestPoints[idp], pos));
                if(curDist < PHOTON_SEARCH_RADIUS)
                {
                    if(offsetplus && idp >= idb)
                    {
                        ++offset;
                        offsetplus = false;
                    }
//                    System.out.println("Idp: " + idp + "Idb: " + idb);
//                    System.out.println("Size of cur: " + cur.returnChildrenAmount());
//                    System.out.println("Testing node: " + cur.returnChild(idp + offset).returnData().toString());
                    reachNodes.add(cur.returnChild(idp + offset));
                }
            }
            
        }
//        if(reachNodes.size() == 0)
//        {
//            System.out.println("Something has gone horribly wrong! reachNodes is empty!");
//            System.out.println("Position: " + pos);
//            System.out.println("Supermax: " + superMax);
//            System.out.println("Supermin: " + superMin);
//        }
        for(int idn = 0; idn < reachNodes.size(); ++idn)
        {
            boolean checkDiameters = false;
            double[] curDiameters = reachNodes.get(idn).returnData().getDiameters();
            for(int idd = 0; idd < 3; ++idd)
            {
//                System.out.println("Comparing: " + curDiameters[idd]);
                if(curDiameters[idd] < PHOTON_SEARCH_RADIUS)
                {
                    checkDiameters = true;
                    break;
                }
            }
            if(!checkDiameters && reachNodes.get(idn).checkIfParent())
            {
                result.addAll(0, getPhotons(pos, reachNodes.get(idn)));
            }
            else if(checkDiameters)
            {
                PhotonContainer curContainer = reachNodes.get(idn).returnData();
//                if(pos.y == 6)
//                {
//                    System.out.println("Hit northern wall!");
//                    System.out.println("current diameters: " + curDiameters[0] + ", " + curDiameters[1] + ", " + curDiameters[2]);
//                    System.out.println("Container size: " + curContainer.getContainerSize());
//                }
                for(int idp = 0; idp < curContainer.getContainerSize(); ++idp)
                {
                    Photon curPhoton = curContainer.getPhoton(idp);
                    double curDist = returnLength(VektorSubtraktion(pos, curPhoton.position));
                    
//                    System.out.println("curPhoton: " + curPhoton.toString());
                    if(curDist < PHOTON_SEARCH_RADIUS && curPhoton.photonType != Photon.PHOTON_SHADOW)
                    {
//                        System.out.println("Adding photon.");
                        result.add(curPhoton);
                    }
                }
            }
        }
        return result;
    }
    
    public static Vertex VektorProdukt(Vertex p0, Vertex p1, Vertex p2)
    {
        double x, y, z;
        Vertex a, b;
        a = new Vertex(p1.x - p0.x, p1.y - p0.y, p1.z - p0.z);
        b = new Vertex(p2.x - p0.x, p2.y - p0.y, p2.z - p0.z);
        x = (a.y*b.z) - (a.z*b.y);
        y = (a.z*b.x) - (a.x*b.z);
        z = (a.x*b.y) - (a.y*b.x);
        return new Vertex(x, y, z);
    }
    
    
    public static ColorDbl PhotonLightCalculationsMesh(Vertex strikept, int TriangleID)
    {
//        for(int idt = 0; idt < Scene.objects[0].getSize(); ++idt)
//        {
//            System.out.println("Colours: " + Scene.objects[0].returnTriangleByIndex(idt).color + ", IDT: " + idt);
//        }
        Vector<Photon> photonList = new Vector<Photon>();
        photonList = getPhotons(strikept, octreeRoot);
//        if(photonList.size() == 0)
//        {
//            System.out.println("No photons found here");
//            System.out.println("Strikept: " + strikept);
//            System.out.println("TriangleID: " + TriangleID);
//        }
//        if(photonList.size() > 0)
//            System.out.println("PhotonList size: " + photonList.size());
        
        double intensity = 0;
        double dist = 0;
        for(int idp = 0; idp < photonList.size(); ++idp)
        {
//            System.out.println("Adding flux!");
//            intensity += photonList.get(idp).flux;
            dist = returnLength(VektorSubtraktion(photonList.get(idp).position,strikept));
            intensity += vladPhotonWeight(photonList.get(idp).flux, dist);
            if(vladPhotonWeight(photonList.get(idp).flux, dist)>photonList.get(idp).flux)
            {
                System.out.println("weight increases flux. (it shouldn't I think. flux: "+
                        photonList.get(idp).flux+", weight: "+vladPhotonWeight(photonList.get(idp).flux, dist));
            }
        }
        if(photonList.size()>0)
        {
            intensity /= photonList.size();
        }
//        if(photonList.size() != 0)
//            intensity = intensity/photonList.size();
        int objectIndex = Scene.getObjectByTriangleIndex(TriangleID);
        
        ColorDbl triangleColor = new ColorDbl(Scene.objects[objectIndex].returnTriangleById(TriangleID).color.r,
        Scene.objects[objectIndex].returnTriangleById(TriangleID).color.g,
        Scene.objects[objectIndex].returnTriangleById(TriangleID).color.b);
        if(Scene.objects[objectIndex].returnTriangleById(TriangleID).triangleIndex == -1)
        {
            System.out.println("Faulty triangle, triangle not found.");
        }
        if(intensity > 100000)
        {
            System.out.println("Triangleindex: " + TriangleID);
            System.out.println("objectIndex: " + objectIndex);
            System.out.println("Caught triangle: " + Scene.objects[objectIndex].returnTriangleById(TriangleID).toString());
            System.out.println("Intensity: " + intensity);
            System.out.println("Pre Color: " + triangleColor);
            System.out.println("Final Color: " + triangleColor);
        }
        triangleColor.setIntensity(intensity);
        return triangleColor;
    }
    
    public static ColorDbl PhotonLightCalculationsSphere(int sphereIndex, Vertex strikept)
    {
//        System.out.println("Calculating photons for sphere!");
        Vector<Photon> photonList = new Vector<Photon>();
        photonList = getPhotons(strikept, octreeRoot);
        double intensity = 0;
        double dist = 0;
        for(int idp = 0; idp < photonList.size(); ++idp)
        {
//            intensity += photonList.get(idp).flux;
            dist = returnLength(VektorSubtraktion(photonList.get(idp).position,strikept));
            intensity += vladPhotonWeight(photonList.get(idp).flux, dist);
        }
        if(photonList.size()>0)
        {
            intensity /= photonList.size();
        }
        Sphere getSphere = (Sphere)Scene.objects[sphereIndex];
        ColorDbl sphereColor = getSphere.color;
        sphereColor.setIntensity(intensity);
        if(sphereColor.r>100000)
        {
            System.out.println("COLOR IS HUUUUUUGE!!!! "+ sphereColor.toString());
        }
//        System.out.println("Spherecolor: " + sphereColor);
//        System.out.println("Photons gathered: " + photonList.size());
//        System.out.println("Intensity: " + intensity);
        return sphereColor;
    }
    
//    public static double formfactor()
//    {
//        return 0;
//    }
    
    public static double BRDF(Triangle t, int reflector_type, HemisCoords vIn, HemisCoords vOut)
    {
        double result;
        Vertex bisectorOfLight = Vertex.DUMMY, h_projection = Vertex.DUMMY, t_line = Vertex.DUMMY;
        double t_var, u_var, v_var, vprim_var, w_var, d, s, diff_wavelength, fresnel_wavelength, non_light_obstruction, facet_slope_dist;
        switch(reflector_type)
        {
            case Triangle.REFLECTION_LAMBERTIAN:
                result = t.reflectionCoefficient/Math.PI;
                break;
            case Triangle.REFLECTION_ORENNAYAR:
                double standardDeviation = ORENNAYAR_STANDARD_DEVIATION;
                double A = 1 - (Math.pow(standardDeviation, 2)/2*(Math.pow(standardDeviation, 2) + 0.33));
                double B = (0.45*Math.pow(standardDeviation, 2)/(Math.pow(standardDeviation, 2) + 0.09));
                double alf = Math.max(vIn.theta, vOut.theta);
                double beta = Math.min(vIn.theta, vOut.theta);
                result = (t.reflectionCoefficient/Math.PI)*(A+B*(Math.max(0, Math.cos(vIn.phi - vOut.phi)))*Math.sin(alf)*Math.sin(beta));
                break;
//            case Triangle.REFLECTION_COOKTORRANCE:
//                bisectorOfLight = VektorAddition(
//                        VektorMultiplikation(hemisToCart(vIn), returnLength(hemisToCart(vOut))), 
//                        VektorMultiplikation(hemisToCart(vOut), returnLength(hemisToCart(vIn))));
//                h_projection = projection_getter(t, vertexToDir(bisectorOfLight));
//                t_line = tangent_getter(t, vertexToDir(bisectorOfLight));
//                t_var = SkalärProdukt(bisectorOfLight, dirToVertex(t.normal));
//                u_var = SkalärProdukt(bisectorOfLight, hemisToCart(vOut));
//                v_var = SkalärProdukt(hemisToCart(vOut), dirToVertex(t.normal));
//                vprim_var = SkalärProdukt(hemisToCart(vIn), dirToVertex(t.normal));
//                w_var = SkalärProdukt(t_line, h_projection);
//                d = 0;
//                s = 1;
//                diff_wavelength = 0.5;
//                fresnel_wavelength = 0.5;
//                non_light_obstruction = 0.9;
//                facet_slope_dist = 100;
//                result = ((d/Math.PI)*diff_wavelength) + 
//                        (s/(4*Math.PI)*SkalärProdukt(hemisToCart(vOut), hemisToCart(vIn)))*
//                        fresnel_wavelength*non_light_obstruction*facet_slope_dist;
////                double w_var = SkalärProdukt(surface tangent vector and projection of bisector on surface);
//                break;
//            case Triangle.REFLECTION_WARD:
//                bisectorOfLight = VektorAddition(
//                        VektorMultiplikation(hemisToCart(vIn), returnLength(hemisToCart(vOut))), 
//                        VektorMultiplikation(hemisToCart(vOut), returnLength(hemisToCart(vIn))));
//                h_projection = projection_getter(t, vertexToDir(bisectorOfLight));
//                t_line = tangent_getter(t, vertexToDir(bisectorOfLight));
//                t_var = SkalärProdukt(bisectorOfLight, dirToVertex(t.normal));
//                u_var = SkalärProdukt(bisectorOfLight, hemisToCart(vOut));
//                v_var = SkalärProdukt(hemisToCart(vOut), dirToVertex(t.normal));
//                vprim_var = SkalärProdukt(hemisToCart(vIn), dirToVertex(t.normal));
//                w_var = SkalärProdukt(t_line, h_projection);
//                double m = 0.2, n = 0.2;
//                double exponent = ((Math.pow(t_var, 2) - 1)/Math.pow(t_var, 2)) * 
//                        ((Math.pow(w_var, 2)/Math.pow(m, 2)) + ((1 - Math.pow(w_var, 2))/Math.pow(n, 2)));
//                facet_slope_dist = (1/m*n)*Math.exp(exponent);
//                diff_wavelength = 0;
//                s = 1;
//                d = 0;
//                fresnel_wavelength = 0.5;
//                result = ((d/Math.PI)*diff_wavelength) + 
//                        (s/(4*Math.PI*Math.sqrt(SkalärProdukt(hemisToCart(vOut), hemisToCart(vIn)))))*
//                        fresnel_wavelength*facet_slope_dist;
//                break;
            default:
                result = 0;
                break;
        }
        return result;
    }
    
    public static double BRDFsphere(Sphere s, Vertex endpt, int reflector_type, HemisCoords vIn, HemisCoords vOut)
    {
        double result;
        switch(reflector_type)
        {
            case Triangle.REFLECTION_LAMBERTIAN:
                result = s.reflectionCoefficient/Math.PI;
                break;
            case Triangle.REFLECTION_ORENNAYAR:
                double standardDeviation = ORENNAYAR_STANDARD_DEVIATION;
                double A = 1 - (Math.pow(standardDeviation, 2)/2*(Math.pow(standardDeviation, 2) + 0.33));
                double B = (0.45*Math.pow(standardDeviation, 2)/(Math.pow(standardDeviation, 2) + 0.09));
                double alf = Math.max(vIn.theta, vOut.theta);
                double beta = Math.min(vIn.theta, vOut.theta);
                result = (s.reflectionCoefficient/Math.PI)*(A+B*(Math.max(0, Math.cos(vIn.phi - vOut.phi)))*Math.sin(alf)*Math.sin(beta));
                break;
//                case Triangle.REFLECTION_COOKTORRANCE:
////                Vertex bisectorOfLight = VektorAddition(
////                        VektorMultiplikation(hemisToCart(vIn), returnLength(hemisToCart(vOut))), 
////                        VektorMultiplikation(hemisToCart(vOut), returnLength(hemisToCart(vIn))));
////                double t_var = SkalärProdukt(bisectorOfLight, dirToVertex(t.normal));
////                double u_var = SkalärProdukt(bisectorOfLight, hemisToCart(vOut));
////                double v_var = SkalärProdukt(hemisToCart(vOut), dirToVertex(t.normal));
////                double vprim_var = SkalärProdukt(hemisToCart(vIn), dirToVertex(t.normal));
//                double d = 0, s_var = 1;
//                double diff_wavelength = 0.5;
//                double fresnel_wavelength = 0.5;
//                double non_light_obstruction = 0.9;
//                double facet_slope_dist = 100;
//                result = ((d/Math.PI)*diff_wavelength) + 
//                        (s_var/(4*Math.PI)*SkalärProdukt(hemisToCart(vOut), hemisToCart(vIn)))*
//                        fresnel_wavelength*non_light_obstruction*facet_slope_dist;
////                double w_var = SkalärProdukt(surface tangent vector and projection of bisector on surface);
//                break;
//            case Triangle.REFLECTION_WARD:
//                result = 0;
//                break;
            default:
                result = 0;
                break;
        }
        return result;
    }
    
//    public static 
    
    public static Direction calculateVectorDirection(Vertex s, Vertex e)
    {
        Direction result = new Direction(e.x - s.x, e.y - s.y, e.z - s.z);
        return normalize(result);
    }
    
    public static Vertex hemisToCart(HemisCoords h)
    {
        Vertex result = new Vertex(0, 0, 0);
        result.x = h.r*Math.cos(h.phi)*Math.sin(h.theta);
        result.y = h.r*Math.sin(h.phi)*Math.sin(h.theta);
        result.z = h.r*Math.cos(h.theta);
        return result;
    }
    
    public static HemisCoords cartToHemis(Vertex cart)
    {
        HemisCoords result = new HemisCoords();
        result.r = returnLength(cart);
        //Theta def. range is [0, (Math.pi/2)].
//        if(Math.acos(cart.z/returnLength(cart)) > (Math.PI/2) && Math.acos(cart.z/returnLength(cart)) < 0)
//        {
//            return new HemisCoords();
//        }
        result.theta = Math.acos(cart.z/result.r);
        //Phi def. range is [0, 2*Math.pi[
        if(cart.y == 0 && cart.x == 0)
            result.phi = 0;
        else
            result.phi = Math.atan(cart.y/cart.x);
        while(result.phi >= 2*Math.PI)
        {
            result.phi -= 2*Math.PI;
        }
        while(result.phi < 0)
        {
            result.phi += 2*Math.PI;
        }
//        if(result.phi >= Math.PI*2)
//            while(result.phi > Math.PI*2)
//                result.phi -= Math.PI*2;
        return result;
    }
    
    public static HemisCoords cartToHemis(Direction cart)
    {
        HemisCoords result = new HemisCoords();
        result.r = returnLength(dirToVertex(cart));
        result.theta = Math.acos(cart.z/result.r);
        if(cart.y == 0 && cart.x == 0)
            result.phi = 0;
        else
            result.phi = Math.atan(cart.y/cart.x);
        while(result.phi >= 2*Math.PI)
        {
            result.phi -= 2*Math.PI;
        }
        while(result.phi < 0)
        {
            result.phi += 2*Math.PI;
        }
        return result;
    }
    
    public static Direction normalize(Direction dir)
    {
        if(dir.equals(Direction.DUMMY))
            return dir;
        double length = Math.sqrt(Math.pow(dir.x, 2) + Math.pow(dir.y, 2) + Math.pow(dir.z, 2));
        dir.x = dir.x/length;
        dir.y = dir.y/length;
        dir.z = dir.z/length;
        return dir;
    }
    
    public static Vertex dirToVertex(Direction dir)
    {
        return new Vertex(dir.x, dir.y, dir.z);
    }
    
    public static Direction vertexToDir(Vertex p)
    {
        return new Direction(p.x, p.y, p.z);
    }
    
    public static Vertex normalize(Vertex vr)
    {
        double length = Math.sqrt(Math.pow(vr.x, 2) + Math.pow(vr.y, 2) + Math.pow(vr.z, 2));
        vr.x = vr.x/length;
        vr.y = vr.y/length;
        vr.z = vr.z/length;
        return vr;
    }
    
    public static Vertex invert(Vertex vr)
    {
        Vertex result = new Vertex(-vr.x, -vr.y, -vr.z);
        return result;
    }
    
    public static Direction invert(Direction dir)
    {
        Direction result = new Direction(-dir.x, -dir.y, -dir.z);
        return result;
    }
    
    public static Vertex reflect(Vertex vr, Direction norm)
    {
        Vertex result = new Vertex(0, 0, 0);
        Vertex vrnorm = dirToVertex(norm);
        return result;
    }
    
    public static Vertex KryssProdukt(Vertex a, Vertex b)
    {
        double x, y, z;
        x = (a.y*b.z) - (a.z*b.y);
        y = (a.z*b.x) - (a.x*b.z);
        z = (a.x*b.y) - (a.y*b.x);
        return new Vertex(x, y, z);
    }
    
    public static double SkalärProdukt(Vertex p0, Vertex p1)
    {
        double result;
        result = (p0.x * p1.x) + (p0.y * p1.y) + (p0.z * p1.z);
        return result;
    }
    
    public static Vertex VektorAddition(Vertex p0, Vertex p1)
    {
        return new Vertex(p0.x + p1.x, p0.y + p1.y, p0.z + p1.z);
    }
    
    public static Vertex VektorSubtraktion(Vertex p0, Vertex p1)
    {
        return new Vertex(p0.x - p1.x, p0.y - p1.y, p0.z - p1.z);
    }
    
    public static Vertex VektorMultiplikation(Vertex p, double m)
    {
        return new Vertex(m*p.x, m*p.y, m*p.z);
    }
    
    public static Vertex VektorMultiplikation(Vertex p0, Vertex p1)
    {
        return new Vertex(p0.x*p1.x, p0.y*p1.y, p0.z*p1.z);
    }
    
    public static Boolean VektorDistansJämförelse(Vertex p0, Vertex p1, Vertex start)
    {
        Vertex dist0 = VektorSubtraktion(p0, start);
        Vertex dist1 = VektorSubtraktion(p1, start);
        if(returnLength(dist0) < returnLength(dist1))
            return true;
        else
            return false;
    }
    public static double VektorKvadratLängd(Vertex p0)
    {
        return p0.x*p0.x + p0.y*p0.y + p0.z*p0.z;
    }
    
    public static double returnLength(Vertex p)
    {
        return Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2) + Math.pow(p.z, 2));
    }
    
    public static double SnellsLaw(double n1, double n2, double angle1)
    {
//        System.out.println("Sneeeeels lååååååå");
        return Math.asin((n1*Math.sin(angle1))/n2);
    }
    
    public static double BrewsterAngle(double n1, double n2)
    {
        return Math.asin(n2/n1);
    }
    
    public static ColorDbl getLightIntensity(Direction normal, Vertex endpt, PointLightSource ls, int triangleID)
    {
//        getMCAreaLightIntensity(normal, endpt, ls, triangleID);
        return getPointLightIntensity(normal, endpt, ls, triangleID);
    }
    public static ColorDbl getPhotonLight(Direction normal, Vertex endpt, int triangleID)
    {
        Vector<Photon> photons = getPhotons(endpt, octreeRoot);
        double flux = 0;
        double r;
        for (Photon photon : photons) 
        {
            if(photon.photonType ==Photon.PHOTON_DIRECT)
            {
                r = returnLength(VektorSubtraktion(photon.position,endpt));
                flux += dummyPhotonWeight(photon.flux, r);
            }
        }
        flux /= photons.size();
        
        ColorDbl retval = new ColorDbl(flux);
        
        return retval;
    }
    public static double lecturePhotonWeight(double flux, double dist)
    {
        double Q = 1;
        double w = Q/(Math.PI*Math.pow(dist, 2));
        return w;
    }
    public static double vladPhotonWeight(double flux, double dist)
    {
        double Q = flux;
        double w = Q/(1+(Math.PI*Math.pow(dist, 2)));
        return w;
    }
    public static double dummyPhotonWeight(double flux, double dist)
    {
        return 1;
    }
    public static ColorDbl getMCAreaLightIntensity(Direction normal, Vertex endpt, int triangleID)
    {
        ColorDbl intensity = new ColorDbl();
        Vertex axis1;
        Vertex axis2;
        Triangle triangle;
        double length1, length2;
        
        PointLightSource fakePoint;
        
        Vertex endPoint;
        ColorDbl returnedIntensity;
        double nHits =0;
        double G =0;
        
        for(int i =0; i< Scene.objects.length; i++)
        {
            if(Scene.objects[i].isLightsource() && !Scene.objects[i].isSphere())
            {
                nHits++;
                for(int r = 0; r<Camera.N_AREALIGHTSOURCEPOINTS; r++)
                {
                    triangle = Scene.objects[i].returnTriangleByIndex(0);
                    axis1 = VektorSubtraktion(triangle.p[1],triangle.p[0]);
                    axis2 = VektorSubtraktion(triangle.p[2],triangle.p[0]);
                    length1 = Math.random();
                    length2 = Math.random();
                    while(length1+length2>1)
                    {
                        length1 = Math.random();
                        length2 = Math.random();
                    }

                    //endpoint = p0 + axis1*length1 + axis2*length2
                    endPoint = triangle.p[0];
                    endPoint = VektorAddition(VektorMultiplikation(axis1,length1), endPoint);
                    endPoint = VektorAddition(VektorMultiplikation(axis2,length2), endPoint);


                    if((endPoint.x > triangle.p[0].x && endPoint.x > triangle.p[1].x && 
                            endPoint.x > triangle.p[2].x) || (endPoint.x < triangle.p[0].x 
                            && endPoint.x < triangle.p[1].x && endPoint.x < triangle.p[2].x))
                    {
                        System.out.println(endPoint);
                    }

                    fakePoint = new PointLightSource(endPoint, triangle.color.meanIntensity()/Scene.LIGHTCOLOR.meanIntensity());
                    returnedIntensity = getPointLightIntensity(normal, endpt,fakePoint , triangleID);
                    double area = 0.5*Math.abs(SkalärProdukt(axis1,axis2));
//                    System.out.println("area "+area);
//                    System.out.println("\n\nArea"+area+"\n\n");
//                    returnedIntensity.setIntensity(1/Camera.N_AREALIGHTSOURCEPOINTS);
//                    G = SkalärProdukt(dirToVertex(normal), VektorSubtraktion(fakePoint.pos,endpt))/
//                            (returnLength(VektorSubtraktion(fakePoint.pos,endpt))*returnLength(dirToVertex(normal)));
                    G = SkalärProdukt(dirToVertex(triangle.normal), VektorSubtraktion(endpt,fakePoint.pos))/
                            (returnLength(VektorSubtraktion(endpt,fakePoint.pos))*returnLength(dirToVertex(triangle.normal)));
                    if(Camera.AREALIGHTAFFECTOR)
                    {
                        if(G<0)
                        {
                            G=0;
                        }
//                    G /=Math.pow(returnLength(VektorSubtraktion(endpt,fakePoint.pos)),2);
                    }
                    else
                    {
                        
                        if(G<0)
                        {
                            G=0;
                        }
                        else
                        {
                            G = 1;
                        }
                    }
                    returnedIntensity.setIntensity(area/Camera.N_AREALIGHTSOURCEPOINTS);
                    returnedIntensity.setIntensity(G);
                    intensity.addColor(returnedIntensity);
                }
            }
        }
//        System.out.println("Intensity "+intensity);
//        intensity.setIntensity((double)1/((double)Camera.N_AREALIGHTSOURCEPOINTS*nHits));
////        System.out.println("Intensity "+intensity+"\n-----------------\n");
        
        if(intensity.meanIntensity()>100)
        {
            System.out.println(intensity);
        }
//                    System.out.println("Intensity " + intensity);
        return intensity;
    }
    public static Vertex randomPointOnTriangle(Triangle triangle)
    {
        Vertex axis1;
        Vertex axis2;
        double length1;
        double length2;
        Vertex endPoint;
        axis1 = VektorSubtraktion(triangle.p[1],triangle.p[0]);
        axis2 = VektorSubtraktion(triangle.p[2],triangle.p[0]);
        length1 = Math.random();
        length2 = Math.random();
        while(length1+length2>1)
        {
            length1 = Math.random();
            length2 = Math.random();
        }

        //endpoint = p0 + axis1*length1 + axis2*length2
        endPoint = triangle.p[0];
        endPoint = VektorAddition(VektorMultiplikation(axis1,length1), endPoint);
        endPoint = VektorAddition(VektorMultiplikation(axis2,length2), endPoint);
        
        return endPoint;
    }
    public static ColorDbl OLDgetMCAreaLightIntensity(Direction normal, Vertex endpt, int triangleID)
    {
        int nrays = 5;
        ColorDbl intensity = new ColorDbl();
        Vertex axis1;
        Vertex axis2;
        Triangle triangle;
        double length1, length2;
        
        ColorDbl tmpCol = new ColorDbl();
        PointLightSource fakePoint;
        
        Vertex endPoint;
        ColorDbl supertemporary;
        for(int i =0; i< Scene.objects.length; i++)
        {
            if(Scene.objects[i].isLightsource() && !Scene.objects[i].isSphere())
            {
                tmpCol = ColorDbl.BLACK; //get rid of garbage vals from prev iteration
                //at this moment ONLY runs for the FIRST triangle in mesh
                
                for(int rays=0; rays<nrays; rays++)
                {
                    triangle = Scene.objects[i].returnTriangleByIndex(0);
                    axis1 = VektorSubtraktion(triangle.p[1],triangle.p[0]);
                    axis2 = VektorSubtraktion(triangle.p[2],triangle.p[0]);
                    length1 = Math.random();
                    length2 = Math.random();
                   
                    //endpoint = p0 + axis1*length1 + axis2*length2
                    endPoint = triangle.p[0];
                    endPoint = VektorAddition(VektorMultiplikation(axis1,length1), endPoint);
                    endPoint = VektorAddition(VektorMultiplikation(axis2,length2), endPoint);
                    
                    fakePoint = new PointLightSource(endPoint, triangle.color.meanIntensity()/100);
                    supertemporary=getPointLightIntensity(normal, endpt,fakePoint , triangleID);
                    System.out.println("Point: ");
                    System.out.println(fakePoint.color);
                    System.out.println(fakePoint.pos);
                    System.out.println(fakePoint.radiance);
                    System.out.println("supertemporary "+supertemporary);
                    tmpCol.addColor(supertemporary);
                    
                }
                double mean = (double)1/nrays;
//                System.out.println("mean "+mean);
                tmpCol.setIntensity(mean);
//                if(tmpCol.meanIntensity()>0.5)
//                System.out.println(tmpCol);
                intensity.addColor(tmpCol);
            }
        }
        return intensity;
    }
    
    public static ColorDbl getPointLightIntensity(Direction normal, Vertex endpt, PointLightSource ls, int triangleID)
    {
        Vertex n = dirToVertex(normal);
        Vertex l = ls.getLightVectorFrom(endpt);
        Ray shadowRay = new Ray(endpt, ls.pos, new ColorDbl(0, 0, 0), -1, Ray.RAY_SHADOW);
        ++Camera.nrRays;
        
        for(int ids = 0; ids < Scene.objects.length; ++ids)
        {
            boolean test = Scene.objects[ids].shadowRayIntersection(shadowRay, ls, triangleID);
            if(test)
            {
                return ColorDbl.BLACK;
            }
        }
        double angle =  SkalärProdukt(n,l)/(returnLength(n)*returnLength(l));
//        if(angle < 0)
//        {
//            System.out.println("Endpt  " + endpt);
//            System.out.println("Scalar " +SkalärProdukt(n,l));
//            System.out.println("Length normal"+ returnLength(n));
//            System.out.println("Length light "+returnLength(l));
//            System.out.println("Multi  " + returnLength(n)*returnLength(l));
//            System.out.println("Normal " + normal.toString());
//            System.out.println("Light  " + l.toString());
//            System.out.println("Angle  " + angle);
//        }
//            System.out.println("Angle: " + angle);
//            System.out.println("pos: " + endpt.toString());
        ColorDbl res = new ColorDbl(ls.color);
        res.setIntensity(angle);
//        if(res.meanIntensity()>100)
//        {
//            System.out.println(res);
//            System.out.println("length n "+returnLength(n));
//            System.out.println("length l "+returnLength(l));
//        }
        if(Double.isInfinite(res.r) || Double.isInfinite(res.g) || Double.isInfinite(res.b))
        {
            System.out.println(res);
            System.out.println("length n "+returnLength(n));
            System.out.println("length l "+returnLength(l));
        }
        return res;
    }
    
    public static ColorDbl getLightIntensity(Direction normal, Vertex endpt, Mesh[] ls, int triangleID)
    {
        ColorDbl res = new ColorDbl();
        return res;
    }
    
    public static double Möller_Trumbore(Vertex pe, Vertex ps, Vertex p0, Vertex p1, Vertex p2)
    {
        double t, u, v;
        
        Vertex T  = VektorSubtraktion(ps,p0);
        Vertex E1 = VektorSubtraktion(p1,p0);
        Vertex E2 = VektorSubtraktion(p2,p0);
        Vertex D  = VektorSubtraktion(pe,ps);
        Vertex P  = KryssProdukt(D,E2);
        Vertex Q  = KryssProdukt(T,E1);
        Double dela = SkalärProdukt(P,E1);
//        System.out.println("Värden:");
//        System.out.println(T.toString());
//        System.out.println(E1.toString());
//        System.out.println(D.toString());
//        System.out.println(Q.toString());
//        System.out.println(P.toString());
//        System.out.println(E2.toString());
//        System.out.println("Dela: " + dela);
//        System.out.println(SkalärProdukt(Q,D));

//        if(!dela.equals(0.0) && !dela.equals(-0.0))
//        {
//            System.out.println(dela);
//        }
        
        t = (SkalärProdukt(Q,E2)/ dela);
        u = (SkalärProdukt(P,T)/ dela);
        v = (SkalärProdukt(Q,D)/ dela);
//        System.out.println("t: " + t);
//        System.out.println("u: " + u);
//        System.out.println("v: " + v);
        
        //error calc
        if(u < 0 || v < 0 || u + v > 1)
        {
            t = -1;
        }
        
        return t;
    }
    
    public static Vertex rotateXVertex(Vertex p, double angle)
    {
        double nx = p.x;
        double ny = (p.y*Math.cos(angle)) + (p.z*Math.sin(angle));
        double nz = (-p.y*Math.sin(angle)) + (p.z*Math.cos(angle));
        return new Vertex(nx, ny, nz);
    }
    
    public static Vertex rotateYVertex(Vertex p, double angle)
    {
        double nx = (p.x*Math.cos(angle)) + (p.z*Math.sin(angle));
        double ny = p.y;
        double nz = (-p.x*Math.sin(angle)) + (p.z*Math.cos(angle));
        return new Vertex(nx, ny, nz);
    }
    
    public static Vertex rotateZVertex(Vertex p, double angle)
    {
        double nx = (p.x*Math.cos(angle)) + (p.y*Math.sin(angle));
        double ny = (-p.x*Math.sin(angle)) + (p.y*Math.cos(angle));
        double nz = p.z;
        return new Vertex(nx, ny, nz);
    }
    
    public static Vertex translateVertex(Vertex p, Vertex dir, double length)
    {
        Vertex result;
        Vertex normDir = normalize(dir);
        result = VektorAddition(p, VektorMultiplikation(normDir, length));
        return result;
    }
    
    public static double VektorVinkel(Vertex v1, Vertex v2)
    {
        return SkalärProdukt(v1,v2)/(returnLength(v1)*returnLength(v2));
    }
    
    
    
    public static double VektorVinkel(Direction d1, Direction d2)
    {
        
        return Math.acos(SkalärProdukt(dirToVertex(d1), dirToVertex(d2))/(returnLength(dirToVertex(d1))*returnLength(dirToVertex(d2))));
    }
    
//    public static Vertex perspectiveProjection(Triangle t, Vertex start, Vertex p)
//    {
//        
//        return new Vertex(0, 0, 0);
//    }
//    
//    public static HemisCoords CalculateHemisCoords(Direction normal, Vertex incoming, Triangle t)
//    {
//        Vertex n = dirToVertex(normal);
//        double theta = Math.acos(VektorVinkel(n, incoming));
//        
//        return new HemisCoords(theta, 0, 0);
//    }
    public static Vertex projection_getter(Triangle tri, Direction hLine)
    {
        Vertex hl = dirToVertex(hLine);
        Vertex v1 = VektorSubtraktion(tri.p[1],tri.p[0]);
        Vertex v2 = VektorSubtraktion(tri.p[2],tri.p[0]);
        
        double mult = SkalärProdukt(hl, v1)/SkalärProdukt(v1,v1);
        Vertex h_v1 = VektorMultiplikation(v1,mult);
        
        mult = SkalärProdukt(hl, v2)/SkalärProdukt(v2,v2);
        Vertex h_v2 = VektorMultiplikation(v2,mult);
        
        Vertex hLineProjection = VektorAddition(h_v1,h_v2);
        return hLineProjection;
    }
    
    public static Vertex tangent_getter(Triangle tri, Vertex inLight)
    {
        Vertex v1 = VektorSubtraktion(tri.p[1],tri.p[0]);
        Vertex v2 = VektorSubtraktion(tri.p[2],tri.p[0]);
        
        double mult = SkalärProdukt(inLight, v1)/SkalärProdukt(v1,v1);
        Vertex h_v1 = VektorMultiplikation(v1,mult);
        
        mult = SkalärProdukt(inLight, v2)/SkalärProdukt(v2,v2);
        Vertex h_v2 = VektorMultiplikation(v2,mult);
        
        Vertex tangentProjection = VektorAddition(h_v1,h_v2);
        
        tangentProjection = VektorSubtraktion(tangentProjection,VektorMultiplikation(tangentProjection,2));
        tangentProjection = normalize(tangentProjection);
        return tangentProjection;
    }
    
    public static Vertex projection_getter_sphere(Sphere sph, Vertex rayEnd, Direction hLine)
    {
        Vertex normal = VektorSubtraktion(rayEnd,sph.center);
        
        Vertex vi = new Vertex(1,0,0);
        //if normal parallell med x-planet
        if(Math.abs(normal.y) < EPSILON && Math.abs(normal.z) < EPSILON)
        {
            vi = new Vertex(0,1,0);
        }
        
        Vertex vi_n = VektorMultiplikation(normal,SkalärProdukt(vi, normal)/SkalärProdukt(normal,normal));
        
        Vertex v1 = normalize(VektorSubtraktion(vi,vi_n));
        Vertex v2 = KryssProdukt(v1,normal);
        
        Vertex hl = dirToVertex(hLine);
        
        double mult = SkalärProdukt(hl, v1)/SkalärProdukt(v1,v1);
        Vertex h_v1 = VektorMultiplikation(v1,mult);
        
        mult = SkalärProdukt(hl, v2)/SkalärProdukt(v2,v2);
        Vertex h_v2 = VektorMultiplikation(v2,mult);
        
        Vertex h = VektorAddition(h_v1,h_v2);
        return h;
    }
    public static HemisCoords HemisAddition(HemisCoords a1, HemisCoords a2)
    {
        HemisCoords retval = new HemisCoords(a1.theta+a2.theta,a1.phi+a2.phi,a1.r+a2.r);
        retval = negativeAngleFixer(retval);
        return retval;
    }
    public static HemisCoords HemisSubtraction(HemisCoords a1, HemisCoords a2)
    {
        HemisCoords retval = new HemisCoords(a1.theta-a2.theta,a1.phi-a2.phi,a1.r-a2.r);
        retval = negativeAngleFixer(retval);
        return retval;
    }
    public static HemisCoords negativeAngleFixer(HemisCoords in)
    {
        double phi = in.phi;
        double theta = in.theta;
        while(phi<0)
            phi+=Math.PI*2;
        while(theta<0)
            theta+=Math.PI*2;
        return new HemisCoords(theta, phi, in.r);
            
    }
    public static Vertex rotateVertexAroundAxis(Vertex point, Direction axis, double angle)
    {
        Vertex u = dirToVertex(axis);
        double newx = (Math.cos(angle)+Math.pow(u.x,2)*(1-Math.cos(angle)))*point.x;
        newx += (u.x*u.y*(1-Math.cos(angle)))*point.y;
        newx += (u.x*u.z*(1-Math.cos(angle))+u.y*Math.sin(angle))*point.z;
        
        double newy = (u.y*u.x*(1-Math.cos(angle))+u.z*Math.signum(angle))*point.x;
        newy += (Math.cos(angle)+Math.pow(u.y, 2)*(1-Math.cos(angle)))*point.y;
        newy += (u.y*u.z*(1-Math.cos(angle))-u.x*Math.sin(angle))*point.z;
        
        double newz = (u.z*u.x*(1-Math.cos(angle)))*point.x;
        newz += (u.z*u.y*(1-Math.cos(angle))+u.x*Math.sin(angle))*point.y;
        newz += (Math.cos(angle)+Math.pow(u.z, 2)*(1-Math.cos(angle)))*point.z;
        return new Vertex(newx,newy,newz);
    }

    public static Vertex roundPosition(Vertex point, Vertex comparison)
    {
        Vertex result = new Vertex(point);
        Vertex difference = VektorSubtraktion(point, comparison);
        difference.x = Math.abs(difference.x);
        difference.y = Math.abs(difference.y);
        difference.z = Math.abs(difference.z);
        if(difference.x < EPSILON)
        {
            result.x = comparison.x;
        }
        if(difference.y < EPSILON)
        {
            result.y = comparison.y;
        }
        if(difference.z < EPSILON)
        {
            result.z = comparison.z;
        }
        return result;
    }
    
    public static Vertex roundPosition(Vertex point)
    {
        Vertex result = new Vertex(point);
        if(point.x > Scene.sceneMax.x)
            result.x = Math.round(point.x);
        if(point.x < Scene.sceneMin.x)
            result.x = Math.round(point.x);
        if(point.y > Scene.sceneMax.y)
            result.y = Math.round(point.y);
        if(point.y < Scene.sceneMin.y)
            result.y = Math.round(point.y);
        if(point.z > Scene.sceneMax.z)
            result.z = Math.round(point.z);
        if(point.z < Scene.sceneMin.z)
            result.z = Math.round(point.z);
        return result;
    }
    
    public static boolean russianRoullette(double flux)
    {
        boolean result = false;
        if(Math.random() < flux)
        {
            result = true;
        }
        return result;
    }
    
}
